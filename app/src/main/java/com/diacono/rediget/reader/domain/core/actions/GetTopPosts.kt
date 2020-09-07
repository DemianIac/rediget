package com.diacono.rediget.reader.domain.core.actions

import com.diacono.rediget.reader.domain.services.PostService

class GetTopPosts(private val postService: PostService) {

    operator fun invoke(limit: Int) = postService.getTop(limit)
}
