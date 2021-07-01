package net.tiap.testappinterview.apis

import net.tiap.testappinterview.models.LoginModel
import net.tiap.testappinterview.models.Product
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Users {

    @POST("auth/login/")
    fun login(@Body loginModel: LoginModel): Call<ResponseBody>

    @GET("products/")
    fun getProducts(): Call<List<Product>>

    @GET("products/categories/")
    fun getCategories(): Call<List<String>>

    @GET("products/category/{categori}")
    fun getProducts(@Path("categori") category: String): Call<List<Product>>
}