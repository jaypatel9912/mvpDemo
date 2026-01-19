package com.mvpdemo.di

import com.mvpdemo.model.remote.repository.PostRepository
import com.mvpdemo.model.remote.repository.PostRepositoryImpl
import com.mvpdemo.presenter.PostContract
import com.mvpdemo.presenter.PostPresenter
import org.koin.dsl.module

val repositoryModule  = module {

    single<PostRepository> { PostRepositoryImpl(get()) }

    factory<PostContract.Presenter> {
        PostPresenter(get())
    }
}