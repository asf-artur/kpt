package com.example.ktp.model

import com.example.ktp.constants.ThinkingErrorsConstants

enum class ThinkingError {
    DichotomousThinking,
    Catastrophization,
    DevaluationOfThePositive,
    EmotionalJustification,
    Labeling,
    MagnificationMinimization,
    ThoughtFilter,
    MindReading,
    Overgeneralization,
    Personalization,
    Appointment,
    TunnelThinking,
}

// Дихотомическое мышление
// Катастрофизация
// Обесценивание позитивного
// Эмоциональное обоснование
// Навешивание ярлыков
// Магнификация/минимизация
// Мысленный фильтр
// Чтение мыслей
// Сверхгенерализация
// Персонализация
// Долженствование
// Туннельное мышление

fun ThinkingError.toRusString(): String {
    return ThinkingErrorsConstants.Map[this]!!
}