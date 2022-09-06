package com.example.session2apk.Network

import android.util.Log
import com.example.session2apk.Helper.HTTP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class CallServicesJD {

    companion object {
        var ruta = "http://10.0.2.2:3121"
        enum class method {
            POST, GET, DELETE, PUT
        }

        fun startQuery(address:String, method: method, data:String, Services:ServicesJD){
            CoroutineScope(Dispatchers.IO).launch {
                Log.e("TAG", "startQuery: $data" )
                var client = URL("$ruta/$address").openConnection() as HttpURLConnection
                client.requestMethod = method.name
                if(method== Companion.method.POST || method == Companion.method.PUT){
                    client.doOutput = true
                    client.useCaches = true
                    client.setRequestProperty("content-type","application/json")
                    client.outputStream.write(data.encodeToByteArray())
                }
                if(client.errorStream!=null){
                    Log.e("TAG", "startQuery:${client.responseCode} " )
                    if(client.responseCode == HTTP.BadRequest){
                        Services.Error("Error en el API",client.responseCode)
                        return@launch
                    }
                    client.errorStream.bufferedReader().use {
                        Services.Error(it.readLine(),client.responseCode)
                    }
                    return@launch
                }
                if(client.inputStream!=null){
                    client.inputStream.bufferedReader().use {
                        Services.Finish(it.readLine(),client.responseCode)
                    }
                    return@launch
                }
            }
        }
    }
}