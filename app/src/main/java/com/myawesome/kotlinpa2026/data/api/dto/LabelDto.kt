package com.myawesome.kotlinpa2026.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LabelDto(
    val id: Int,
    val name: String,
    val color: String?,
    @Json(name = "color_active") val colorActive: String?
)

@JsonClass(generateAdapter = true)
data class LabelsResponse(
    val data: List<LabelDto>,
    val total: Int
)
