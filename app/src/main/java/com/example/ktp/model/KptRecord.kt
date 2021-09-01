package com.example.ktp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Запись КПТ
 */
@Entity
class KptRecord(
    /**
     * Ситуация
     */
    val situation: String,
    val automaticThought: String?,
    /**
     * Эмоциональные реакции
     */
    val emotionalReactions: List<String>?,
    /**
     * Телесные реакции
     */
    val bodilyReactions: String?,
    val behavior: String?,
    val truthOfThought: Double?,
    /**
     * Ошибки мышления
     */
    val thinkingErrors: List<ThinkingError>?,
    val creationDate: Calendar,
    val changeDate: Calendar,
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}