package com.myawesome.kotlinpa2026.ui.months

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myawesome.kotlinpa2026.data.api.dto.MonthEntryDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthListScreen(
    onMonthClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onOnThisDayClick: () -> Unit,
    onNewPostClick: () -> Unit,
    onBack: () -> Unit,
    vm: MonthListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    if (state.selectedYear != null) {
        BackHandler { vm.clearYear() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.selectedYear ?: "Pa2026") },
                navigationIcon = {
                    IconButton(
                        onClick = if (state.selectedYear != null) vm::clearYear else onBack
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.selectedYear == null) {
                        IconButton(onClick = onOnThisDayClick) {
                            Icon(Icons.Default.DateRange, contentDescription = "On This Day")
                        }
                        IconButton(onClick = onSearchClick) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewPostClick) {
                Icon(Icons.Default.Add, contentDescription = "New post")
            }
        }
    ) { padding ->
        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text(state.error ?: "Error")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::load) { Text("Retry") }
                }
            }

            state.selectedYear == null -> LazyColumn(contentPadding = padding) {
                items(state.years) { year ->
                    YearItem(year = year, onClick = { vm.selectYear(year) })
                    HorizontalDivider()
                }
            }

            else -> LazyColumn(contentPadding = padding) {
                items(state.monthsForSelectedYear) { month ->
                    MonthItem(month = month, onClick = { onMonthClick(month.ym) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun YearItem(year: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(year) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun MonthItem(month: MonthEntryDto, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(month.month) },
        trailingContent = {
            Text(
                "${month.count}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
