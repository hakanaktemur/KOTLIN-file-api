package com.hrblizz.fileapi.data.repository

import com.hrblizz.fileapi.data.entities.Entity
import org.springframework.data.mongodb.repository.MongoRepository

interface FileRepository : MongoRepository<Entity,Long>