package com.myawesome.kotlinpa2026.ui.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class PostEditorUiState(
    val body: String = "",
    val date: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PostEditorViewModel @Inject constructor(
    private val repository: DiaryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int? = savedStateHandle.get<Int>("id")

    private val _uiState = MutableStateFlow(PostEditorUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (postId != null) loadPost(postId)
    }

    private fun loadPost(id: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            runCatching { repository.getPosts(200).find { it.id == id } }
                .onSuccess { post ->
                    if (post != null) {
                        _uiState.value = _uiState.value.copy(
                            body = post.body,
                            date = LocalDate.parse(post.date.take(10)),
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun onBodyChange(value: String) {
        _uiState.value = _uiState.value.copy(body = value, error = null)
    }

    fun onDateChange(value: LocalDate) {
        _uiState.value = _uiState.value.copy(date = value)
    }

    fun save() {
        val state = _uiState.value
        if (state.body.isBlank()) {
            _uiState.value = state.copy(error = "Body cannot be empty")
            return
        }
        _uiState.value = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                if (postId != null) {
                    repository.updatePost(postId, state.body, state.date)
                } else {
                    repository.createPost(state.body, state.date)
                }
            }.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
