package com.hrblizz.fileapi

import com.hrblizz.fileapi.controller.FileController
import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.rest.FileEntity
import com.hrblizz.fileapi.rest.FileUploadRequest
import javafx.beans.binding.Bindings.`when`
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify
import jdk.internal.vm.compiler.word.LocationIdentity.any
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.bind.Bindable.listOf
import org.springframework.boot.context.properties.bind.Bindable.mapOf
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.LocalDateTime
import java.util.*
import javax.management.Query.times

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test :-)
     */

     private lateinit var fileRepository: FileRepository
     private lateinit var fileController: FileController
    
    @Test
    fun shouldAnswerWithTrue() {
        assertTrue(true)
    }

    @BeforeEach
    fun setUp() {
        fileRepository = mock(FileRepository::class.java)
        fileController = FileController(fileRepository)
    }

    @Test
    fun testUploadFile() {
        val file = MockMultipartFile(
            "file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
            "Test file content".toByteArray()
        )
        val uploadRequest = FileUploadRequest(
            name = file.originalFilename ?: "",
            contentType = file.contentType ?: "",
            meta = mapOf("creatorEmployeeId" to "1"),
            content = file
        )

        val response = fileController.uploadFile(uploadRequest)

        assertEquals(HttpStatus.CREATED.value(), response.status)
        val responseBody = response.body
        assertNotNull(responseBody)
        val token = responseBody["token"]
        assertNotNull(token)

        verify(fileRepository, times(1)).save(any(FileEntity::class.java))
    }

    @Test
    fun testGetFileMetadata() {
        val token1 = UUID.randomUUID().toString()
        val token2 = UUID.randomUUID().toString()
        val tokens = listOf(token1, token2)
        val fileEntity1 = FileEntity(
            token = token1,
            filename = "example1.pdf",
            size = 1024L,
            contentType = "application/pdf",
            createTime = LocalDateTime.now(),
            meta = mapOf("creatorEmployeeId" to "1")
        )
        val fileEntity2 = FileEntity(
            token = token2,
            filename = "example2.txt",
            size = 512L,
            contentType = "text/plain",
            createTime = LocalDateTime.now(),
            meta = mapOf("creatorEmployeeId" to "2")
        )
        `when`(fileRepository.findAllById(tokens)).then(listOf(fileEntity1, fileEntity2))

        val response = fileController.getFileMetadata(tokens)

        assertEquals(HttpStatus.OK.value(), response.status)
        val responseBody = response.body
        assertNotNull(responseBody)
        val files = responseBody.files
        assertNotNull(files)
        assertEquals(2, files.size)
        val metadata1 = files[token1]
        assertNotNull(metadata1)
        assertEquals(token1, metadata1.token)
        assertEquals("example1.pdf", metadata1.filename)

        verify(fileRepository, times(1)).findByTokenIn(tokens)
    }



}
