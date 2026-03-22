package com.myawesome.kotlinpa2026.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentDto(
    val id: Int,
    val date: String,
    val body: String,
    @Json(name = "post_id") val postId: Int
)

@JsonClass(generateAdapter = true)
data class PeriodDto(
    val id: Int,
    val name: String,
    val start: String?,
    val end: String?
)

@JsonClass(generateAdapter = true)
data class PostDto(
    val id: Int,
    val body: String,
    val date: String,
    val comments: List<CommentDto> = emptyList(),
    val labels: List<Int> = emptyList(),
    val periods: List<PeriodDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PostsResponse(
    val data: List<PostDto>,
    val total: Int
)

@JsonClass(generateAdapter = true)
data class CreatePostRequest(
    val body: String,
    val date: String
)

@JsonClass(generateAdapter = true)
data class CreateCommentRequest(
    val body: String,
    @Json(name = "post_id") val postId: Int,
    val date: String
)
