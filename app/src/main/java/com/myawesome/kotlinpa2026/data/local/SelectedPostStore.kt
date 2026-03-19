package com.myawesome.kotlinpa2026.data.local

import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedPostStore @Inject constructor() {
    private val _post = MutableStateFlow<PostDto?>(null)
    val post: StateFlow<PostDto?> = _post.asStateFlow()

    fun setPost(post: PostDto) { _post.value = post }
    fun clear() { _post.value = null }
}
