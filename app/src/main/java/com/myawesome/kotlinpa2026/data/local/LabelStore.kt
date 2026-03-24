package com.myawesome.kotlinpa2026.data.local

import com.myawesome.kotlinpa2026.data.api.dto.LabelDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelStore @Inject constructor() {
    private val _labels = MutableStateFlow<List<LabelDto>>(emptyList())
    val labels: StateFlow<List<LabelDto>> = _labels.asStateFlow()

    fun set(labels: List<LabelDto>) { _labels.value = labels }
    fun isLoaded() = _labels.value.isNotEmpty()
}
