package com.hrblizz.fileapi.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class FileDownloadResponseEntity<T>(
    var fileContent: T?,
    var filename: String,
    var fileSize: Long,
    var createTime: String,
    var contentType: String,
    var status: HttpStatus
) : ResponseEntity<T>(fileContent,status.value() ) {

    private var _headers: HttpHeaders

    init {
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        headers.set("X-Filename", filename)
        headers.set("X-Filesize", fileSize.toString())
        headers.set("X-CreateTime", createTime)
        headers.contentType = MediaType.parseMediaType(contentType)

        this._headers = headers
    }

    constructor(
        fileContent: T?,

    ) : this(fileContent, filename, fileSize, createTime, contentType) {
        this.status = status
    }
}
