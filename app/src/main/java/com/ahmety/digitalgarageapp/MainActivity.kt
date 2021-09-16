package com.ahmety.digitalgarageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private lateinit var buttonReq: Button
    lateinit var hashString:String
    lateinit var mail: String
    private lateinit var textViewLoading: TextView
    private lateinit var textViewEmail: TextView
    var myMail: String = "ahmetyilmaz_gs@hotmail.com"
    var twoWords : String = ""
    var arry = arrayOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9",".",":",",",";",
        "`","!","'","^","+","%","&","/","(",")","=","?","-","_","*","£","#","$","½","{","[","]","}","\\","<",">","|","@")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonReq = findViewById(R.id.buttonRequest)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewLoading = findViewById(R.id.textViewLoading)
        mail=""

        buttonReq.setOnClickListener{
            putRequest()
        }
       // putRequest()

    }
    private fun putRequest(){
        textViewLoading.visibility = View.VISIBLE
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://career.dijitalgaraj.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service
        val service = retrofit.create(ApiService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("GUID", "a8acd4ba-9b2a-438e-bab4-c3dff899a2ff")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {

            // Do the PUT request and get response
            val response = service.putGuid(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val items = response.body()
                    if (items != null) {
                        hashString = items.hash.toString()
                        Log.d("Hash :", hashString)
                        var startIndex = 0
                        var endIndex = 32
                       // var len = 15
                        var len = hashString.length
                        len= len/32
                        var eachSubStr = ""
                        for(x in 1..len){
                            val substring = hashString.subSequence(startIndex, endIndex)
                            var substringStr = substring.toString()
                            var isMatchedHash = false
                            for (x in arry){
                                for (y in arry){
                                    twoWords = eachSubStr + x + y
                                    val myMailHash = toMD5Hash(myMail).toLowerCase().replace("-","")
                                    val mdTwoWords = toMD5Hash(twoWords).toLowerCase().replace("-","")
                                    val fullWord = myMailHash + twoWords + mdTwoWords
                                    var finalMD = toMD5Hash(fullWord).toLowerCase().replace("-","")
                                    Log.d("mdFirstTwow :", twoWords)
                                    if (finalMD == substringStr){
                                        Log.d("final :", finalMD)
                                        isMatchedHash = true
                                        break
                                    }
                                }
                                if (isMatchedHash){
                                    break
                                }
                            }

                            startIndex = startIndex + 32
                            endIndex = endIndex + 32
                            mail = twoWords
                            eachSubStr = twoWords
                            //Log.d("mail :", mail)
                        }
                        textViewLoading.visibility = View.GONE
                        textViewEmail.setText("Email = " + mail)
                    }

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    // md5 çevirme işlemi yapan fonksiyon
    private fun byteArrayToHexString( array: Array<Byte> ): String {

        var result = StringBuilder(array.size * 2)

        for ( byte in array ) {

            val toAppend =
                String.format("%2X", byte).replace(" ", "0") // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'

        return result.toString()
    }

    private fun toMD5Hash( text: String ): String {

        var result = ""

        try {

            val md5 = MessageDigest.getInstance("MD5")
            val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()

            result = byteArrayToHexString(md5HashBytes)
        }
        catch ( e: Exception ) {

            result = "error: ${e.message}"
        }

        return result
    }

    // md5 çevirme işlemi yapan fonksiyon
}