package com.diacono.rediget.reader.di

import androidx.lifecycle.SavedStateHandle
import com.diacono.rediget.reader.presentation.PostDetailFragment
import com.diacono.rediget.reader.presentation.PostListActivity
import com.diacono.rediget.reader.presentation.PostListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val readerModule = module {
    factory { PostDetailFragment() }
    scope<PostListActivity> {
        viewModel { (handle: SavedStateHandle) -> PostListViewModel(handle, get()) }
    }
}