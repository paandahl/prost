package com.tripfinger.commons.prost

import com.tripfinger.commons.prost.annotations.Guard
import com.tripfinger.commons.prost.annotations.Open
import com.tripfinger.commons.prost.annotations.RestMethod
import com.tripfinger.commons.prost.model.HttpResponse

@Guard
object GuardedRestHandler {

    @JvmStatic
    @Open
    @RestMethod("/apple")
    fun getFruits(): HttpResponse {
        return HttpResponse(200, "Apple", null)
    }

    @JvmStatic
    @RestMethod("/hello/:name")
    fun sayHello(name: String): HttpResponse {
        val response = HttpResponse()
        response.body = "Hello, $name"
        return response
    }
}