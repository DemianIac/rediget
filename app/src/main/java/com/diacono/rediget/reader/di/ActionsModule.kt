package com.diacono.rediget.reader.di

import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import org.koin.dsl.module

val actionsModule = module {
    factory { GetTopPosts(get()) }
}