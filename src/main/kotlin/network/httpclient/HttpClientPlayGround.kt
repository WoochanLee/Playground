package network.httpclient

import makeOkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class NetworkPlayground(isLogging: Boolean) {

    private val client = makeOkHttpClient(isLogging)

    fun requestSimpleGET(
        url: String,
        isPrintHeader: Boolean = false,
        isPrintBody: Boolean = false,
        isPrintException: Boolean = false
    ) {
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            response.printResult(isPrintHeader, isPrintBody)
        } catch (e: Exception) {
            if (isPrintException) e.printStackTrace()
        }
    }

    fun requestJsonPOST(
        url: String,
        json: String,
        isPrintHeader: Boolean = false,
        isPrintBody: Boolean = false,
        isPrintException: Boolean = false
    ) {
        val body = json.toRequestBody()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build();

        try {
            val response = client.newCall(request).execute()
            response.printResult(isPrintHeader, isPrintBody)
        } catch (e: Exception) {
            if (isPrintException) e.printStackTrace()
        }
    }

    private fun Response.printResult(
        isPrintHeader: Boolean = false,
        isPrintBody: Boolean = false
    ) {
        if (isPrintHeader) printHeader()
        if (isPrintBody) printBody()
    }

    private fun Response.printHeader() = println(headers.toString())
    private fun Response.printBody() = println(body?.string())
}

fun main() {
    val playground = NetworkPlayground(true)

    playground.requestSimpleGET("https://www.google.com")
    playground.requestJsonPOST("https://github.com/WoochanLee", "{\"name\":\"Woody\"}")
}