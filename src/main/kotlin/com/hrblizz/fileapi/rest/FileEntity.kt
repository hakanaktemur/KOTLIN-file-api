package com.hrblizz.fileapi.rest

import java.time.LocalDateTime

class FileEntity<T> : ResponseEntity<T> {
    var token: String
    var filename: String
    var size: Long
    var contentType: String
    var createTime: LocalDateTime
    var meta: Map<String, Any>

    constructor(
        token: String,
        filename: String,
        size: Long,
        contentType: String,
        createTime: LocalDateTime,
        meta: Map<String, Any>,
        data: T?,
        status: Int
    ) : super(data, status) {
        this.token = token
        this.filename = filename
        this.size = size
        this.contentType = contentType
        this.createTime = createTime
        this.meta = meta
    }
}
