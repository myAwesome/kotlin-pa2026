package com.myawesome.kotlinpa2026.data.repository

import com.myawesome.kotlinpa2026.data.api.ApiService
import com.myawesome.kotlinpa2026.data.api.dto.*
import com.myawesome.kotlinpa2026.data.local.SessionStore
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val api: ApiService,
    private val sessionStore: SessionStore
) {
    suspend fun login(email: String, password: String): String {
        val response = api.login(AuthRequest(email = email, password = password))
        sessionStore.saveToken(response.accessToken)
        return response.accessToken
    }

    suspend fun logout() = sessionStore.clearToken()

    suspend fun getToken(): String? = sessionStore.token.first()

    suspend fun getPosts(limit: Int = 50): List<PostDto> =
        api.getPosts(limit = limit).data

    suspend fun createPost(body: String, date: LocalDate): PostDto {
        val formatted = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .replace("T", " ")
        return api.createPost(CreatePostRequest(body = body, date = formatted))
    }

    suspend fun updatePost(id: Int, body: String, date: LocalDate): PostDto {
        val formatted = date.atStartOfDay()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return api.updatePost(id, CreatePostRequest(body = body, date = formatted))
    }

    suspend fun searchPosts(query: String, limit: Int = 50): List<PostDto> =
        api.searchPosts(query = "%$query%", limit = limit).data

    suspend fun getHistory(): List<MonthEntryDto> = api.getHistory()

    suspend fun getPostsByMonth(ym: String): List<PostDto> = api.getPostsByMonth(ym)

    suspend fun getOnThisDay(monthDay: String): List<PostDto> = api.getOnThisDay(monthDay)

    suspend fun getLabels(): List<LabelDto> = api.getLabels().data
}
