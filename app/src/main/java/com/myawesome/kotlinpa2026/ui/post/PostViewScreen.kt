package com.myawesome.kotlinpa2026.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myawesome.kotlinpa2026.data.api.dto.CommentDto
import com.myawesome.kotlinpa2026.data.api.dto.LabelDto
import com.myawesome.kotlinpa2026.data.api.dto.PostDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostViewScreen(
    postId: Int,
    onEditClick: () -> Unit,
    onBack: () -> Unit,
    vm: PostViewViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.post?.date?.take(10) ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
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
            ) { Text(state.error ?: "Error") }

            state.post != null -> PostContent(
                post = state.post!!,
                labels = state.labels,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun PostContent(
    post: PostDto,
    labels: List<LabelDto>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(post.body, style = MaterialTheme.typography.bodyLarge)

        if (post.periods.isNotEmpty()) {
            Text("Periods", style = MaterialTheme.typography.labelMedium)
            post.periods.forEach { period ->
                SuggestionChip(onClick = {}, label = { Text(period.name) })
            }
        }

        if (post.labels.isNotEmpty()) {
            val labelNames = post.labels.mapNotNull { id -> labels.find { it.id == id }?.name }
            if (labelNames.isNotEmpty()) {
                Text("Labels", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    labelNames.forEach { name ->
                        SuggestionChip(onClick = {}, label = { Text(name) })
                    }
                }
            }
        }

        if (post.comments.isNotEmpty()) {
            HorizontalDivider()
            Text("Comments", style = MaterialTheme.typography.titleSmall)
            post.comments.forEach { comment ->
                CommentItem(comment)
            }
        }
    }
}

@Composable
private fun CommentItem(comment: CommentDto) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            comment.date.take(10),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(comment.body, style = MaterialTheme.typography.bodyMedium)
    }
}
