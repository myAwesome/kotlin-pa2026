package com.myawesome.kotlinpa2026.ui.onthisday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import com.myawesome.kotlinpa2026.data.local.SelectedPostStore
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class OnThisDayUiState(
    val posts: List<PostDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnThisDayViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val selectedPostStore: SelectedPostStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnThisDayUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun selectPost(post: PostDto) = selectedPostStore.setPost(post)

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        val today = LocalDate.now()
        val md = today.format(DateTimeFormatter.ofPattern("MM-dd"))
        viewModelScope.launch {
            runCatching { repository.getOnThisDay(md) }
                .onSuccess { posts ->
                    _uiState.value = _uiState.value.copy(posts = posts, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
