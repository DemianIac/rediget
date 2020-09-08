package com.diacono.rediget.reader.domain.core.actions

import com.diacono.rediget.reader.domain.services.PostService

class GetMoreTopPosts(private val postService: PostService) {

    operator fun invoke(limit: Int, after: String) = postService.getMoreTop(limit, after)
}
