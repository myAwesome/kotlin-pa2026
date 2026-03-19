package com.myawesome.kotlinpa2026.ui.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.LabelDto
import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostViewUiState(
    val post: PostDto? = null,
    val labels: List<LabelDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PostViewViewModel @Inject constructor(
    private val repository: DiaryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(PostViewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                val postDeferred = async { repository.getPost(postId) }
                val labelsDeferred = async { repository.getLabels() }
                Pair(postDeferred.await(), labelsDeferred.await())
            }.onSuccess { (post, labels) ->
                _uiState.value = _uiState.value.copy(
                    post = post,
                    labels = labels,
                    isLoading = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
