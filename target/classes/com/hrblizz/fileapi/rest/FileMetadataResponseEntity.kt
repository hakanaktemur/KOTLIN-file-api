package com.hrblizz.fileapi.rest

class FileMetadataResponseEntity(
    val files: Map<String, FileMetadata>
) : ResponseEntity<Map<String, FileMetadata>> {
    data class FileMetadata(
        val token: String,
        val filename: String,
        val size: Long,
        val contentType: String,
        val createTime: String,
        val meta: Map<String, Any>
    )

    constructor(files: Map<String, FileMetadata>, status: Int) : super(files, status)
}
