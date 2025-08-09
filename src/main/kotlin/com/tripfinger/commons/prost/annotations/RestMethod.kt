package com.tripfinger.commons.prost.annotations

import com.tripfinger.commons.prost.model.HttpMethod

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RestMethod(
    val value: String,
    val method: HttpMethod = HttpMethod.GET
)