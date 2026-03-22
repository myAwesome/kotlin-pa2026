package com.myawesome.kotlinpa2026.ui.months

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myawesome.kotlinpa2026.data.api.dto.MonthEntryDto
import com.myawesome.kotlinpa2026.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MonthListUiState(
    val months: List<MonthEntryDto> = emptyList(),
    val selectedYear: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val years: List<String> get() = months.map { it.year }.distinct().sortedDescending()
    val monthsForSelectedYear: List<MonthEntryDto> get() =
        months.filter { it.year == selectedYear }
}

@HiltViewModel
class MonthListViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonthListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching { repository.getHistory() }
                .onSuccess { months ->
                    _uiState.value = _uiState.value.copy(months = months, isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun selectYear(year: String) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
    }

    fun clearYear() {
        _uiState.value = _uiState.value.copy(selectedYear = null)
    }
}
