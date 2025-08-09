package com.tripfinger.commons.prost.model

class HttpResponse {
    var status: Int = 200
    var body: String? = null
    var contentType: String = "application/json"

    constructor()

    constructor(status: Int, body: String?) {
        this.status = status
        this.body = body
    }
    
    constructor(status: Int, message: String, id: String?) {
        this.status = status
        this.body = String.format("{\"status\": %d, \"message\": \"%s\", \"id\": \"%s\"}", status, message, id)
    }

    class ResponseObject {
        var status: Int = 0
        var message: String? = null
        var id: String? = null
    }
}