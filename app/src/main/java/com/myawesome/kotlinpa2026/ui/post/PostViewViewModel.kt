package com.myawesome.kotlinpa2026.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.CommentDto
import com.myawesome.kotlinpa2026.data.api.dto.LabelDto
import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import com.myawesome.kotlinpa2026.data.local.SelectedPostStore
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostViewUiState(
    val post: PostDto? = null,
    val labels: List<LabelDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val commentInput: String = "",
    val isSubmittingComment: Boolean = false,
    val commentError: String? = null
)

@HiltViewModel
class PostViewViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val selectedPostStore: SelectedPostStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostViewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val post = selectedPostStore.post.value
        if (post != null) {
            _uiState.value = _uiState.value.copy(post = post, isLoading = true)
            loadLabels()
        } else {
            _uiState.value = _uiState.value.copy(error = "No post selected")
        }
    }

    private fun loadLabels() {
        viewModelScope.launch {
            runCatching { repository.getLabels() }
                .onSuccess { labels ->
                    _uiState.value = _uiState.value.copy(labels = labels, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun onCommentInputChange(value: String) {
        _uiState.value = _uiState.value.copy(commentInput = value, commentError = null)
    }

    fun submitComment() {
        val post = _uiState.value.post ?: return
        val body = _uiState.value.commentInput.trim()
        if (body.isEmpty()) return

        _uiState.value = _uiState.value.copy(isSubmittingComment = true, commentError = null)
        viewModelScope.launch {
            runCatching { repository.createComment(postId = post.id, body = body) }
                .onSuccess { newComment ->
                    val updatedComments = (_uiState.value.post?.comments ?: emptyList()) + newComment
                    val updatedPost = _uiState.value.post?.copy(comments = updatedComments)
                    _uiState.value = _uiState.value.copy(
                        post = updatedPost,
                        commentInput = "",
                        isSubmittingComment = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSubmittingComment = false,
                        commentError = e.message
                    )
                }
        }
    }
}
