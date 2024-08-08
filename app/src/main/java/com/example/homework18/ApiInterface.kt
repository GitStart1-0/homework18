package com.example.homework18

import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/all.json")
    fun getSuperheroes(): Single<List<Superhero>>
}