package com.mvpdemo.model.remote.repository

import com.mvpdemo.model.remote.models.Post

interface PostRepository {
    suspend fun fetchPosts(): List<Post>
}
