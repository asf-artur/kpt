package com.example.ktp.constants

import com.example.ktp.model.ThinkingError

class ThinkingErrorsConstants {
    companion object{
        val Value = listOf(
            ThinkingError.DichotomousThinking to "Дихотомическое мышление",
            ThinkingError.Catastrophization to "Катастрофизация",
            ThinkingError.DevaluationOfThePositive to "Обесценивание позитивного",
            ThinkingError.EmotionalJustification to "Эмоциональное обоснование",
            ThinkingError.Labeling to "Навешивание ярлыков",
            ThinkingError.MagnificationMinimization to "Магнификация/минимизация",
            ThinkingError.ThoughtFilter to "Мысленный фильтр",
            ThinkingError.MindReading to "Чтение мыслей",
            ThinkingError.Overgeneralization to "Сверхгенерализация",
            ThinkingError.Personalization to "Персонализация",
            ThinkingError.Appointment to "Долженствование",
            ThinkingError.TunnelThinking to "Туннельное мышление",
        )

        val Map = Value.toMap()
    }
}