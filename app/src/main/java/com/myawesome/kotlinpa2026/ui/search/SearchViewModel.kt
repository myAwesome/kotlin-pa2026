package com.myawesome.kotlinpa2026.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.PostDto
import com.myawesome.kotlinpa2026.data.local.SelectedPostStore
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<PostDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val selectedPostStore: SelectedPostStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun selectPost(post: PostDto) = selectedPostStore.setPost(post)

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query, error = null)
        searchJob?.cancel()
        if (query.length < 2) {
            _uiState.value = _uiState.value.copy(results = emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching { repository.searchPosts(query) }
                .onSuccess { results ->
                    _uiState.value = _uiState.value.copy(results = results, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
