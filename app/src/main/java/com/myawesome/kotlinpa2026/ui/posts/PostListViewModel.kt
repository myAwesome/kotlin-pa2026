package com.myawesome.kotlinpa2026.ui.posts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import com.myawesome.kotlinpa2026.data.local.SelectedPostStore
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostListUiState(
    val posts: List<PostDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val selectedPostStore: SelectedPostStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ym: String = checkNotNull(savedStateHandle["ym"])

    private val _uiState = MutableStateFlow(PostListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun selectPost(post: PostDto) = selectedPostStore.setPost(post)

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching { repository.getPostsByMonth(ym) }
                .onSuccess { posts ->
                    _uiState.value = _uiState.value.copy(posts = posts, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
