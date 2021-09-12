package pl.polsl.peoplecounter

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.APIDataProvider.APIService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

class APIDataProvider {

    interface APIService {
        /*@GET("/predicitons")
        fun greetUser(@Path("user") user: String): Call<ResponseBody>*/
        @GET("/predictions")

        fun greetUser(): Call<ResponseBody>

        @Headers("Content-type: application/json")
        @POST("/setup")
        fun getVectors(@Body body: JsonObject): Call<ResponseBody>

    }
    companion object {

        val BASE_URL = "http://192.168.0.241:5000/"

        private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

        var service = retrofit.create(APIService::class.java)
    }
}