package com.hrblizz.fileapi.rest

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest(
    val name: String,
    val contentType: String,
    val meta: Map<String, Any>,
    val source: String,
    val expireTime: String?,
    val content: MultipartFile
)
