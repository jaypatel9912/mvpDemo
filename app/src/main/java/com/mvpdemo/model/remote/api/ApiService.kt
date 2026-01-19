package com.mvpdemo.model.remote.api

import com.mvpdemo.model.remote.models.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val httpClient: HttpClient) {

    suspend fun getPosts() =
        httpClient.get("https://jsonplaceholder.typicode.com/posts").body<List<Post>>()
}
