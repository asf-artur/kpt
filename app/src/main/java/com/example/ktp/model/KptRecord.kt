package com.example.ktp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Запись КПТ
 */
@Entity
class KptRecord(
    @PrimaryKey val id: Int,
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
    val bodilyReactions: List<String>?,
    val behavior: String?,
    val truthOfThought: Double?,
    /**
     * Ошибки мышления
     */
    val thinkingErrors: List<ThinkingError>?,
    val creationDate: Calendar,
    val changeDate: Calendar,
)