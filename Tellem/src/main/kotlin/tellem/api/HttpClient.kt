package tellem.api

import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpClient(private val baseUrl: String?) {
    private val client: HttpClient = HttpClient.newHttpClient()

    @Throws(IOException::class, InterruptedException::class)
    fun get(endpoint: Endpoint): HttpResponse<String?>? {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + endpoint.path))
            .GET()
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    @Throws(IOException::class, InterruptedException::class)
    fun post(endpoint: Endpoint, jsonBody: String, accessToken: String? = null): HttpResponse<String?>? {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + endpoint.path))
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .apply {
                header("Content-Type", "application/json")
                if (accessToken != null) {
                    header("Authorization", "Bearer $accessToken")
                }
            }
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}