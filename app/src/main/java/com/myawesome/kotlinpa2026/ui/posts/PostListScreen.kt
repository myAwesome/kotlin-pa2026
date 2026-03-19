package com.myawesome.kotlinpa2026.ui.posts

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
import com.myawesome.kotlinpa2026.data.api.dto.PostDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    ym: String,
    onPostClick: (Int) -> Unit,
    onBack: () -> Unit,
    vm: PostListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ym) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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
