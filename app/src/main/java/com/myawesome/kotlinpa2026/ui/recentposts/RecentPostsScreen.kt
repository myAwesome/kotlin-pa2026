package com.myawesome.kotlinpa2026.ui.recentposts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myawesome.kotlinpa2026.data.api.dto.PostDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentPostsScreen(
    onPostClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onOnThisDayClick: () -> Unit,
    onMonthsClick: () -> Unit,
    onNewPostClick: () -> Unit,
    vm: RecentPostsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pa2026") },
                actions = {
                    IconButton(onClick = onOnThisDayClick) {
                        Icon(Icons.Default.DateRange, contentDescription = "On This Day")
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onMonthsClick) {
                        Icon(Icons.Default.List, contentDescription = "Months")
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
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.error ?: "Error")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::load) { Text("Retry") }
                }
            }

            else -> LazyColumn(contentPadding = padding) {
                items(state.posts) { post ->
                    PostItem(post = post, onClick = { vm.selectPost(post); onPostClick(post.id) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun PostItem(post: PostDto, onClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                post.date.take(10),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        supportingContent = {
            Text(
                post.body,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
