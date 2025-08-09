package com.tripfinger.commons.prost

import com.tripfinger.commons.prost.annotations.Guard
import com.tripfinger.commons.prost.annotations.RestMethod
import com.tripfinger.commons.prost.model.HttpMethod
import com.tripfinger.commons.prost.model.HttpResponse

object RestHandler {

    @JvmStatic
    @RestMethod("/apple")
    fun getFruits(): HttpResponse {
        return HttpResponse(200, "Apple", null)
    }

    @JvmStatic
    @Guard
    @RestMethod("/apple2")
    fun getFruits2(): HttpResponse {
        return HttpResponse(200, "Apple", null)
    }

    @JvmStatic
    @RestMethod("/hello/:name")
    fun sayHello(name: String): HttpResponse {
        val response = HttpResponse()
        response.body = "Hello, $name"
        return response
    }

    @JvmStatic
    @RestMethod(method = HttpMethod.POST, value = "/bye/:name")
    fun sayBye(name: String, body: String): HttpResponse {
        val response = HttpResponse()
        val times = body.toInt()
        response.body = "Bye, "
        for (i in 1 until times) {
            response.body += "bye, "
        }
        response.body = response.body + name
        return response
    }
}