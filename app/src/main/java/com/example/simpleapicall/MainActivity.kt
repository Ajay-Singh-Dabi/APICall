package com.example.simpleapicall

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsyncTask("ajay","123456").execute()
    }

    private inner class CallAPILoginAsyncTask(val username: String, val password: String): AsyncTask<Any, Void, String>() {

        private lateinit var customProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result: String
            var connection : HttpURLConnection? = null

            try{
                val url = URL("https://run.mocky.io/v3/e4ccfdc4-2c1b-4576-9532-8c639227a991")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true

                connection.instanceFollowRedirects = false

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type","application/json")
                connection.setRequestProperty("charset","utf-8")
                connection.setRequestProperty("Accept", "application/json")

                connection.useCaches = false

                val writeDataOutputStream = DataOutputStream(connection.outputStream)
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username)
                jsonRequest.put("password", password)

                writeDataOutputStream.writeBytes(jsonRequest.toString())
                writeDataOutputStream.flush()
                writeDataOutputStream.close()

                val httpResult: Int = connection.responseCode

                if (httpResult == HttpURLConnection.HTTP_OK){
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(
                        InputStreamReader(inputStream))

                    val stringBuilder = StringBuilder()
                    var line : String?
                    try{
                        while (reader.readLine().also { line = it }!=null){
                            stringBuilder.append(line + "\n")
                        }
                    }catch (e: IOException){
                        e.printStackTrace()
                    }finally {
                        try{
                            inputStream.close()
                        }catch (e: IOException){
                            e.printStackTrace()
                        }
                    }
                    result = stringBuilder.toString()
                }else{
                    result = connection.responseMessage
                }
            }catch (e: SocketTimeoutException){
                result = "Connection TimeOut"
            }catch (e: Exception){
                result = "Error : " + e.message
            }finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgessDialog()

            Log.i("JSON RESPONSE RESULT", result!!)

            val responseData = Gson().fromJson(result, ResponseData::class.java)
            Log.i("Message", responseData.message)
            Log.i("User Id","${responseData.user_id}")
            Log.i("Name", responseData.name)
            Log.i("Email", responseData.email)
            Log.i("Mobile", "${responseData.mobile}")

            Log.i("Is Profile Completed", "${responseData.profile_details.is_profile_completed}")
            Log.i("Rating","${responseData.profile_details.rating}")

            for (item in responseData.data_list.indices){
                Log.i("Value $item", "${responseData.data_list[item]}")

                Log.i("ID","${responseData.data_list[item].id}")
                Log.i("Value","${responseData.data_list[item].value}")
            }

//            val jsonObject = JSONObject(result)
//            val message = jsonObject.optString("message")
//            Log.i("Message", message)
//            val userId = jsonObject.optInt("user_id")
//            Log.i("User Id","$userId")
//            val name = jsonObject.optString("name")
//            Log.i("Message", name)
//
//            val profileDetailsObject = jsonObject.optJSONObject("profile_details")
//            val isProfileCompleted  = profileDetailsObject.optBoolean("is_profile_completed")
//            Log.i("Is Profile Completed", "$isProfileCompleted")
//
//
//            val dataListArray = jsonObject.optJSONArray("data_list")
//            Log.i("Data List Size", "${dataListArray.length()}")
//            for (item in 0 until dataListArray.length()){
//                Log.i("Value $item", "${dataListArray[item]}")
//                val dataItemObject: JSONObject = dataListArray[item] as JSONObject
//
//                val id = dataItemObject.optInt("id")
//                Log.i("id",id.toString())
//                val value = dataItemObject.optString("value")
//                Log.i("value",value)
//            }
        }

        private fun showProgressDialog(){
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)
            customProgressDialog.show()
        }

        private fun cancelProgessDialog(){
            customProgressDialog.dismiss()
        }
    }


}