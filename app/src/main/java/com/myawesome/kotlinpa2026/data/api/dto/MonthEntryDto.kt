package com.myawesome.kotlinpa2026.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MonthEntryDto(
    val ym: String,
    @Json(name = "m") val month: String,
    @Json(name = "y") val year: String,
    val count: Int
)
