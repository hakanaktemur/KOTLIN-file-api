package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.rest.FileDownloadResponseEntity
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
        try {

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
            fileRepository.Save(fileEntity)

            return ResponseEntity(
                mapOf(
                    "token" to _token
                ),
                HttpStatus.CREATED.value()
            )

        }
        catch(Exception ex) {
            ExceptionLogItem("An Exception has been occured", ex)
            throw BadRequestException(ex.message)
        }
        
    }

    @PostMapping("/metas")
    fun getFileMetadata(@RequestBody tokens: List<String>): FileMetadataResponseEntity<Map<String, FileMetadataResponse.FileMetadata>> {
        
        val files = fileRepository.findAllById(tokens) ?: throw NotFoundException("Files Not Found")
        
        val metadata = files.associateBy(
            keySelector = { it.token },
            valueTransform = { fileEntity ->
                FileMetadataResponse.FileMetadata(
                    token = fileEntity.token,
                    filename = fileEntity.filename,
                    size = fileEntity.size,
                    contentType = fileEntity.contentType,
                    createTime = fileEntity.createTime,
                    meta = fileEntity.meta
                )
            }
        )
        return FileMetadataResponseEntity(metadata, HttpStatus.OK.value())
    }


    @GetMapping("/file/{token}")
    fun downloadFile(@PathVariable token: String): FileDownloadResponseEntity<Map<T, Any> {
        
        // Read file from disk
        val fileEntity = fileRepository.findByToken(token) ?: throw NotFoundException("File not found")
        
        val filePath = "$fileUploadDirectory/$token"
        val file = File(filePath)
        if (!file.exists()) {
            Logger(LogItem("File not found"))
            throw NotFoundException("File not found")
        }

        return FileDownloadResponseEntity(fileEntity,
            fileEntity.filename,
            fileEntity.size.toString(),
            fileEntity.createTime.toString(),
            fileEntity.contentType
        )
    }

    @DeleteMapping("/file/{token}")
    fun deleteFile(@PathVariable token: String): ResponseEntity<Unit> {
        
        //Find file from disk
        val fileEntity = fileRepository.findByToken(token) ?: throw NotFoundException("File not found")

        val filePath = "$fileUploadDirectory/$token"
        val file = File(filePath)
        if (!file.exists()) {
            Logger(LogItem("File not found"))
            throw NotFoundException("File not found")
        }

        // Delete file from disk
        fileRepository.deleteFile(token)
        file.Delete()

        return ResponseEntity(
            Unit,
            HttpStatus.NO_CONTENT.value()
        )
    }
}