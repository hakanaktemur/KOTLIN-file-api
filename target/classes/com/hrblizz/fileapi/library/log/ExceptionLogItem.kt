package com.hrblizz.fileapi.library.log

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.PrintWriter
import java.io.StringWriter

open class ExceptionLogItem(
    message: String,
    @JsonIgnore private val exception: Exception
) : LogItem("$message: <${exception::class.java.name}>") {
    val stacktrace: String = StringWriter().apply {
        exception.printStackTrace(PrintWriter(this))
    }.toString()

    init {
        type = "exception"
    }

//    init {
//        this.type = "exception"
//
//        val stringWriter = StringWriter()
//        exception.printStackTrace(PrintWriter(stringWriter))
//        this.stacktrace = stringWriter.toString()
//    }

    override fun toString(): String {
        return "[$dateTime] [$correlationId] $message \n $stacktrace"
    }
}
