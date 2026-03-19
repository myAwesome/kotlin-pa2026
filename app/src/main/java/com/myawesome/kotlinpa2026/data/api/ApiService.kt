package com.myawesome.kotlinpa2026.data.api

import com.myawesome.kotlinpa2026.data.api.dto.*
import retrofit2.http.*

interface ApiService {

    @POST("authentication")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("posts")
    suspend fun getPosts(
        @Query("\$sort[date]") sort: Int = -1,
        @Query("\$limit") limit: Int = 50
    ): PostsResponse

    @POST("posts")
    suspend fun createPost(@Body post: CreatePostRequest): PostDto

    @PATCH("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: CreatePostRequest
    ): PostDto

    @GET("posts")
    suspend fun searchPosts(
        @Query("body[\$like]") query: String,
        @Query("\$limit") limit: Int = 50
    ): PostsResponse

    @GET("posts-history")
    suspend fun getHistory(
        @Query("get") get: String = "months"
    ): List<MonthEntryDto>

    @GET("posts-history")
    suspend fun getPostsByMonth(
        @Query("ym") ym: String
    ): List<PostDto>

    @GET("posts-history")
    suspend fun getOnThisDay(
        @Query("md") md: String
    ): List<PostDto>

    @GET("labels")
    suspend fun getLabels(
        @Query("\$limit") limit: Int = 100
    ): LabelsResponse
}
