package com.mvpdemo.presenter

import com.mvpdemo.model.remote.models.Post
import com.mvpdemo.model.remote.repository.PostRepository
import kotlinx.coroutines.*

class PostPresenter(
    private val repository: PostRepository
) : PostContract.Presenter {

    private var view: PostContract.View? = null

    private var allPosts: List<Post> = emptyList()

    private val presenterJob = Job()

    private val presenterScope: CoroutineScope =
        CoroutineScope(presenterJob + Dispatchers.Main)

    override fun attach(view: PostContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
        presenterJob.cancel()
    }

    override fun loadPosts() {
        view?.showLoading()

        presenterScope.launch {
            try {
                allPosts = withContext(Dispatchers.IO) {
                    repository.fetchPosts()
                }

                view?.hideLoading()
                view?.showPosts(allPosts)

            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    override fun onSearch(query: String) {
        if (query.isBlank()) {
            view?.showPosts(allPosts)
            return
        }

        val filtered = allPosts.filter { post ->
            post.title.contains(query, ignoreCase = true) ||
            post.body.contains(query, ignoreCase = true)
        }

        view?.showPosts(filtered)
    }
}

