package com.mvpdemo.view.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mvpdemo.model.remote.models.Post
import com.mvpdemo.presenter.PostContract
import org.koin.compose.koinInject

@Composable
fun PostScreen() {

    val presenter = koinInject<PostContract.Presenter>()

    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : PostContract.View {

            override fun showPosts(postList: List<Post>) {
                posts = postList
                errorMessage = null
            }

            override fun showLoading() {
                isLoading = true
            }

            override fun hideLoading() {
                isLoading = false
            }

            override fun showError(message: String) {
                errorMessage = message
            }
        }
    }

    DisposableEffect(Unit) {
        presenter.attach(view)
        presenter.loadPosts()

        onDispose {
            presenter.detach()
        }
    }

    Column(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxSize()) {
        Text(
            "Posts",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        SearchBar(onSearch = presenter::onSearch)

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                ErrorView(
                    message = errorMessage!!,
                    onRetry = presenter::loadPosts
                )
            }

            else -> {
                if (posts.isNotEmpty()) {
                    PostList(posts = posts)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No posts available",
                            modifier = Modifier.padding(top = 16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        shape = CircleShape,
        onValueChange = {
            query = it
            onSearch(it)
        },
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        placeholder = { Text("Search posts") },
        singleLine = true
    )
}

@Composable
fun PostList(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LocalContentColor.current, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = post.body,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
