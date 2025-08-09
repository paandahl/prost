package com.tripfinger.commons.prost

import com.tripfinger.commons.prost.annotations.Guard
import com.tripfinger.commons.prost.annotations.Open
import com.tripfinger.commons.prost.annotations.RestMethod
import com.tripfinger.commons.prost.annotations.UrlParam
import com.tripfinger.commons.prost.model.Authorizer
import com.tripfinger.commons.prost.model.HttpMethod
import com.tripfinger.commons.prost.model.HttpResponse
import com.tripfinger.commons.prost.utils.StreamUtils
import com.tripfinger.commons.prost.utils.Tuple
import org.apache.commons.fileupload.FileUploadException
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestHandler : HttpServlet() {

    companion object {
        @JvmField
        var authorizer: Authorizer? = null

        @JvmField
        var restHandlers: MutableMap<String, Tuple<PathEntry, Map<*, *>>> = HashMap()

        @JvmField
        var guardAll = false

        @JvmField
        var guardedMethods: MutableSet<HttpMethod> = HashSet()
    }

    enum class GuardStatus {
        NOT_SPECIFIED,
        GUARDED,
        OPEN
    }

    class MethodEntry {
        var method: Method? = null
        var guardStatus: GuardStatus? = null
    }

    class PathEntry {
        var methods: MutableMap<HttpMethod, MethodEntry> = HashMap()
        var urlParams: List<String> = LinkedList()
    }

    fun setAuthorizer(auth: Authorizer) {
        RequestHandler.authorizer = auth
    }

    fun addMethodGuard(httpMethod: HttpMethod) {
        guardedMethods.add(httpMethod)
    }

    fun setRestHandler(restHandler: Class<*>) {
        guardAll = restHandler.isAnnotationPresent(Guard::class.java)

        for (m in restHandler.declaredMethods) {
            if (m.isAnnotationPresent(RestMethod::class.java)) {
                val annotation = m.getAnnotation(RestMethod::class.java)
                val url = annotation.value
                val pathEntry = PathEntry()
                val methodEntry = MethodEntry()
                methodEntry.method = m
                
                if (!guardAll && m.isAnnotationPresent(Guard::class.java)) {
                    methodEntry.guardStatus = GuardStatus.GUARDED
                } else if (m.isAnnotationPresent(Open::class.java)) {
                    methodEntry.guardStatus = GuardStatus.OPEN
                } else {
                    methodEntry.guardStatus = GuardStatus.NOT_SPECIFIED
                }
                
                pathEntry.methods[annotation.method] = methodEntry
                pathEntry.urlParams = getUrlParamsForMethod(m)
                compileUrlParts(getUrlParts(url), restHandlers, pathEntry)
            }
        }
    }

    @Throws(ServletException::class, IOException::class)
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val url = req.pathInfo
        val httpMethod = HttpMethod.valueOf(req.method)
        val parameterMap = req.parameterMap
        val parameters = HashMap<String, String>()
        
        @Suppress("UNCHECKED_CAST")
        for (key in parameterMap.keys) {
            val keyStr = key as String
            val values = parameterMap[key] as Array<String>
            if (values.isNotEmpty()) {
                parameters[keyStr] = values[0]
            }
        }
        
        writeResponse(resp, handleRequest(url, httpMethod, null, parameters, null))
    }

    @Throws(ServletException::class, IOException::class)
    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        setCorsHeaders(resp)
    }

    @Throws(ServletException::class, IOException::class)
    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        var body: String? = null
        val items = HashMap<String, ByteArray>()
        
        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                val factory = DiskFileItemFactory()
                factory.sizeThreshold = 100_000_000
                val upload = ServletFileUpload(factory)
                val iterator = upload.getItemIterator(req)
                
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    val name = item.fieldName

                    if (!item.isFormField) {
                        items[name] = StreamUtils.readBytesFromInputStream(item.openStream())
                    }
                }
            } catch (e: FileUploadException) {
                throw RuntimeException(e)
            }
        } else {
            body = getPostBodyForRequest(req)
        }

        val url = req.pathInfo
        val httpMethod = HttpMethod.valueOf(req.method)
        val parameters = HashMap<String, String>()
        
        writeResponse(resp, handleRequest(url, httpMethod, body, parameters, items))
    }

    @Throws(ServletException::class, IOException::class)
    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        doGet(req, resp)
    }

    protected fun setCorsHeaders(resp: HttpServletResponse) {
        resp.setHeader("Access-Control-Allow-Origin", "*")
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        resp.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization")
    }

    @Throws(IOException::class)
    protected fun writeResponse(resp: HttpServletResponse, response: HttpResponse) {
        resp.contentType = response.contentType
        resp.setStatus(response.status)
        resp.characterEncoding = StandardCharsets.UTF_8.name()
        setCorsHeaders(resp)

        resp.writer.println(response.body)
    }

    fun handleRequest(
        url: String, 
        httpMethod: HttpMethod, 
        requestBody: String?, 
        parameters: Map<String, String>,
        files: Map<String, ByteArray>?
    ): HttpResponse {
        val urlParts = getUrlParts(url)

        return if (urlParts.size >= 1) {
            handleRequest(httpMethod, urlParts, parameters, requestBody, files)
        } else {
            HttpResponse(404, "URL not valid: $url")
        }
    }

    fun getPostBodyForRequest(req: HttpServletRequest): String {
        return try {
            req.inputStream.use { input ->
                StreamUtils.readStringFromInputStream(input)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    protected fun getUrlParts(url: String): List<String> {
        return listOf(*url.replaceFirst("^/".toRegex(), "").split("/").toTypedArray())
    }

    protected fun compileUrlParts(
        urlParts: List<String>, 
        handlers: MutableMap<String, Tuple<PathEntry, Map<*, *>>>, 
        m: PathEntry
    ) {
        var urlPart = urlParts[0]
        if (urlPart.startsWith(":")) {
            urlPart = "param"
        }
        
        var entry = handlers[urlPart]
        if (entry == null) {
            entry = Tuple(null, null)
            handlers[urlPart] = entry
        }
        
        if (urlParts.size == 1) {
            if (entry.x != null) {
                for ((key, value) in m.methods) {
                    entry.x!!.methods[key] = value
                }
                entry.x!!.urlParams = m.urlParams
            } else {
                entry.x = m
            }
        } else {
            if (entry.y == null) {
                @Suppress("UNCHECKED_CAST")
                entry.y = HashMap<Method, Tuple<Method, Map<*, *>>>()
            }
            @Suppress("UNCHECKED_CAST")
            compileUrlParts(
                urlParts.subList(1, urlParts.size), 
                entry.y as MutableMap<String, Tuple<PathEntry, Map<*, *>>>, 
                m
            )
        }
    }

    protected fun getUrlParamsForMethod(m: Method): List<String> {
        val urlParams = LinkedList<String>()
        val annotationArrays = m.parameterAnnotations
        
        for (parameterAnnotations in annotationArrays) {
            if (parameterAnnotations.isNotEmpty()) {
                val urlParam = parameterAnnotations[0] as UrlParam
                urlParams.add(urlParam.value)
            }
        }
        
        return urlParams
    }

    protected fun getUrlMethod(
        pathElements: List<String>, 
        restParameters: MutableList<Any>,
        handlers: Map<String, Tuple<PathEntry, Map<*, *>>>,
        method: HttpMethod
    ): PathEntry? {
        val pathElement = pathElements[0]
        var element = handlers[pathElement]
        var addParameter = false
        
        if (element == null) {
            addParameter = true
            element = handlers["param"] ?: return null
        }
        
        if (pathElements.size == 1) {
            if (element.x == null) {
                addParameter = true
                element = handlers["param"]
            }
            
            if (element?.x?.methods?.containsKey(method) != true) {
                return null
            }
            
            if (addParameter) {
                restParameters.add(pathElement)
            }
            
            return element.x
        } else {
            if (element.y == null) {
                return null
            }
            
            if (addParameter) {
                restParameters.add(pathElement)
            }
            
            @Suppress("UNCHECKED_CAST")
            var entry = getUrlMethod(
                pathElements.subList(1, pathElements.size), 
                restParameters, 
                element.y as Map<String, Tuple<PathEntry, Map<*, *>>>, 
                method
            )
            
            if (entry == null && addParameter) {
                restParameters.removeAt(restParameters.size - 1)
            }
            
            if (entry == null && pathElement != "param") {
                restParameters.add(pathElement)
                val newPathElements = LinkedList<String>()
                newPathElements.add("param")
                newPathElements.addAll(pathElements.subList(1, pathElements.size))
                
                @Suppress("UNCHECKED_CAST")
                entry = getUrlMethod(
                    newPathElements, 
                    restParameters, 
                    handlers, 
                    method
                )
                
                if (entry == null) {
                    restParameters.removeAt(restParameters.size - 1)
                }
            }
            
            return entry
        }
    }

    fun handleRequest(pathElements: List<String>, parameters: Map<String, String>): HttpResponse {
        return handleRequest(HttpMethod.GET, pathElements, parameters, null, null)
    }

    fun handleRequest(pathElements: List<String>, body: String): HttpResponse {
        return handleRequest(HttpMethod.POST, pathElements, null, body, null)
    }

    fun handleRequest(
        httpMethod: HttpMethod, 
        pathElements: List<String>, 
        parameters: Map<String, String>?, 
        body: String?, 
        files: Map<String, ByteArray>?
    ): HttpResponse {
        val restParameters = LinkedList<Any>()
        val pathEntry = getUrlMethod(pathElements, restParameters, restHandlers, httpMethod)
        
        if (pathEntry == null || !pathEntry.methods.containsKey(httpMethod)) {
            return HttpResponse(404, "Resource not found: $pathElements")
        }

        val m = pathEntry.methods[httpMethod]!!
        
        if (m.guardStatus != GuardStatus.OPEN &&
            (guardAll || guardedMethods.contains(httpMethod) || m.guardStatus == GuardStatus.GUARDED)) {
            
            if (authorizer == null) {
                throw RuntimeException("Method guarded but authorizer not set.")
            }

            if (!authorizer!!.isAuthorized()) {
                return HttpResponse(401, "Method call was not authorized.")
            }
        }

        return try {
            if (httpMethod != HttpMethod.POST) {
                for (urlParameter in pathEntry.urlParams) {
                    restParameters.add(parameters?.get(urlParameter) ?: "")
                }
            } else {
                restParameters.add(body ?: "")

                if (files != null && files.isNotEmpty()) {
                    restParameters.add(files)
                }
            }
            
            val response = m.method!!.invoke(null, *restParameters.toArray()) as HttpResponse?
            
            response ?: HttpResponse(404, "Resource not found: $pathElements")
        } catch (e: Exception) {
            val inner = if (e is InvocationTargetException) e.cause else e
            val errors = StringWriter()
            inner!!.printStackTrace(PrintWriter(errors))
            println(errors.toString())
            HttpResponse(500, inner.toString())
        }
    }
}