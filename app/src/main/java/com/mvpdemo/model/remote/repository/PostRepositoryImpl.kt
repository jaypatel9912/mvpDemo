package com.mvpdemo.model.remote.repository

import com.mvpdemo.model.remote.api.ApiService
import com.mvpdemo.model.remote.models.Post

class PostRepositoryImpl(
    private val api: ApiService
) : PostRepository {

    override suspend fun fetchPosts(): List<Post> {
        return api.getPosts()
    }
}
