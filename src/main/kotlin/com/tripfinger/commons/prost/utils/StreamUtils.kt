package com.tripfinger.commons.prost.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

object StreamUtils {

    @JvmStatic
    fun readBytesFromInputStream(input: InputStream): ByteArray {
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(16384)

        try {
            var nRead: Int
            while (input.read(data, 0, data.size).also { nRead = it } != -1) {
                buffer.write(data, 0, nRead)
            }
            buffer.flush()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return buffer.toByteArray()
    }

    @JvmStatic
    fun readStringFromInputStream(input: InputStream): String {
        val s = Scanner(input, StandardCharsets.UTF_8.name()).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}