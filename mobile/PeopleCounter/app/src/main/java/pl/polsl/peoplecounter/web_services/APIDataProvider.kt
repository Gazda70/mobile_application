package pl.polsl.peoplecounter.web_services

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class APIDataProvider {

    interface APIService {
        @GET("/predictions")
        fun getDetectionStatistics(): Call<ResponseBody>

        @Headers("Content-type: application/json")
        @POST("/setup")
        fun setupNewDetection(@Body body: JsonObject): Call<ResponseBody>
    }
    companion object {
        val BASE_URL = "http://192.168.0.66:5000/"
        private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        var service = retrofit.create(APIService::class.java)
    }
}