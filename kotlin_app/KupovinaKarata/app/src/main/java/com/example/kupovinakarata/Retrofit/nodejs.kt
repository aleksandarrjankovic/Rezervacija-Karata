package com.example.kupovinakarata.Retrofit

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface nodejs {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email: String,
                     @Field("username") username: String,
                     @Field("password") password: String): Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email: String,
                  @Field("password") password: String): Observable<String>

    @POST("rezervacija")
    @FormUrlEncoded
    fun rezervacija(@Field("email") email: String,
                          @Field("username") username: String,
                          @Field("br_karata")br_karata: String,
                          @Field("price")price:String,
                          @Field("tribina")tribina:String,
                          @Field("id")id:String): Observable<String>
    @POST("rezervacijaLogin")
    @FormUrlEncoded
    fun rezervacijaLogin(@Field("email") email:String,
                         @Field("id") id:String,
                         @Field("br_karata") br_karata: String,
                         @Field("price")price:String,
                         @Field("tribina")tribina:String) : Observable<String>
}