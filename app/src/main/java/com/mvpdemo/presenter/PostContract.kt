package com.mvpdemo.presenter

import com.mvpdemo.model.remote.models.Post

interface PostContract {

    interface View {
        fun showPosts(postList: List<Post>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun loadPosts()
        fun onSearch(query: String)
    }
}
