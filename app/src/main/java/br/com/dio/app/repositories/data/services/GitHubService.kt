package br.com.dio.app.repositories.data.services

import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.data.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("users/{user}/repos")
    suspend fun listRepositories(@Path("user") user: String,
                                 @Query("sort") sorting: String?) : List<Repo>
    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String) : Owner


}