package com.tripfinger.commons.prost.model

interface Authorizer {
    fun isAuthorized(): Boolean
}