package com.myawesome.kotlinpa2026.data.api.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LabelDto(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class LabelsResponse(
    val data: List<LabelDto>,
    val total: Int
)
