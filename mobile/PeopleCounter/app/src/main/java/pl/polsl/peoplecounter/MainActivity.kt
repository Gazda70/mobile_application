package pl.polsl.peoplecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.APIDataProvider.Companion.service

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultButton = findViewById<TextView>(R.id.resultField)
        getRequest(resultButton)
    }

    fun postRequest(){
//  POST demo
        val jsonObj = JsonObject()
        jsonObj.addProperty("title", "rhythm")
        jsonObj.addProperty("singer", "meee")
        jsonObj.addProperty("text", "Jack and jill went up the hill to fetch a pail of water!")

        // Convert JSONObject to String
       /* val jsonObjectString = jsonObj.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())*/

        CoroutineScope(Dispatchers.IO).launch {
            val response = APIDataProvider.service.getVectors(jsonObj).execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.toString())

                }
            }
        }
            /*.enqueue(object : Callback<ResponseBody!>! {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("---TTTT :: POST Throwable EXCEPTION:: " + t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.string()
                        println("---TTTT :: POST msg from server :: " + msg)
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })*/
    }

    fun getRequest(resultButton:TextView){
        CoroutineScope(Dispatchers.IO).launch {
            /*
             * For @Query: You need to replace the following line with val response = service.getEmployees(2)
             * For @Path: You need to replace the following line with val response = service.getEmployee(53)
             */

            // Do the GET request and get response
            val response = service.greetUser().execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                    resultButton.setText(prettyJson)
                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {
                    resultButton.setText(response.code().toString())
                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
        /*APIDataProvider
            .service
            .greetUser("Audhil")
            .enqueue(object : Callback{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("---TTTT :: GET Throwable EXCEPTION:: " + t.message)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.string()
                        println("---TTTT :: GET msg from server :: " + msg)
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })*/
    }
}