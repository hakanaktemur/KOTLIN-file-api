package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.rest.FileEntity
import com.hrblizz.fileapi.rest.FileUploadRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.bind.Bindable.mapOf
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
class FileController(
    @Autowired private val fileRepository: FileRepository
) {
    @RequestMapping("/files", method = [RequestMethod.POST])
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@ModelAttribute request: FileUploadRequest) : ResponseEntity<Map<String, Any>>
    {
        val _token = UUID.randomUUID().toString()
        val fileEntity = FileEntity(
            token = _token,
            filename = request.name,
            size = request.content.size.toLong(),
            contentType = request.contentType,
            createTime = LocalDateTime.now(),
            meta = request.meta
        )

        //Save file to disk
        fileRepository.save(fileEntity)

        return ResponseEntity(
            mapOf(
                "token" to _token
            ),
            HttpStatus.CREATED.value()
        )
    }

}