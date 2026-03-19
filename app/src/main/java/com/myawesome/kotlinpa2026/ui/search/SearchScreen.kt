package com.myawesome.kotlinpa2026.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onPostClick: (Int) -> Unit,
    onBack: () -> Unit,
    vm: SearchViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = state.query,
                        onValueChange = vm::onQueryChange,
                        placeholder = { Text("Search...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.results.isEmpty() && state.query.length >= 2 ->
                    Text("No results", Modifier.align(Alignment.Center))
                else -> LazyColumn {
                    items(state.results) { post ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    post.date.take(10),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            supportingContent = {
                                Text(post.body, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            },
                            modifier = Modifier.clickable { vm.selectPost(post); onPostClick(post.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
