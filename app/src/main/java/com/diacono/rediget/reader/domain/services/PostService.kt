package com.diacono.rediget.reader.domain.services

import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    @GET("/top.json")
    fun getTop(@Query("limit") limit: Int): Single<Response<RedditResponse>>

    @GET("/top.json")
    fun getMoreTop(
        @Query("limit") limit: Int,
        @Query("after") after: String
    ): Single<Response<RedditResponse>>
}