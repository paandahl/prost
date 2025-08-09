package com.tripfinger.commons.prost.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class UrlParam(val value: String)