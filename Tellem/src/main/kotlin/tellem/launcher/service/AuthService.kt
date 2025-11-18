package tellem.launcher.service

import tellem.api.HttpClient
import tellem.api.Endpoint
import com.google.gson.JsonParser
import tellem.session.SessionManager
//import tellem.session.TokenEncryptor
import tellem.util.Token
import java.net.http.HttpResponse
import java.time.LocalDateTime

// nie wiem czy korzystanie z funkcji hasJsonResponse jest optymalne, czy nie można parsować jednorazowo, a nie wiele razy wywoływać tą samą operacje
class AuthService(private val client: HttpClient, private val sessionManager: SessionManager) {
    fun login(username: String, password: String?): AuthResult {
        try {
            val json = "{\"username\":\"$username\", \"password\":\"$password\"}"
            val response = client.post(Endpoint.LOGIN, json)!!

            val accessToken = itemJsonResponse(response, "accessToken")
            val accessTokenValidDateString = itemJsonResponse(response, "accessTokenValidDate")
            val accessTokenValidDate: LocalDateTime = LocalDateTime.parse(accessTokenValidDateString)

            val refreshToken = itemJsonResponse(response, "refreshToken")
            val refreshTokenValidDateString = itemJsonResponse(response, "refreshTokenValidDate")
            val refreshTokenValidDate: LocalDateTime = LocalDateTime.parse(refreshTokenValidDateString)

            sessionManager.accessToken = Token(accessToken, accessTokenValidDate)
            sessionManager.refreshToken = Token(refreshToken, refreshTokenValidDate)

            return if (response.statusCode() == 200) {
                AuthResult(true, "You logged in successfully.")
            } else {
                AuthResult(
                    false,
                    "Status: " + response.statusCode() + "\n" + itemJsonResponse(response, "message")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(false, "App error.")
        }
    }

    fun loginWithTokens(): AuthResult? {
        try {
            val json = "{\"accessToken\":\"${sessionManager.accessToken?.value}\"}"
            val response = client.post(Endpoint.LOGIN_TOKEN, json)!!
            if (response.statusCode() == 200) {
                return AuthResult(true, "You logged in successfully.")
            } else if (response.statusCode() == 300) {
                // zmień kod na expired access token
                refresh()
                return loginWithTokens()
            } else {
                return AuthResult(false, "Tokens expired")
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun register(username: String, password: String?, email: String?): AuthResult {
        try {
            val json =
                "{\"username\":\"$username\", \"password\":\"$password\", \"email\":\"$email\"}"
            println(json)
            val response = client.post(Endpoint.REGISTER, json)!!


            return if (response.statusCode() == 201) {
                AuthResult(true, "Account is created. Please check your mail inbox for verification link")
            } else {
                AuthResult(
                    false,
                    "Status: " + response.statusCode() + "\n" + itemJsonResponse(response, "message")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(false, "App error.")
        }
    }

    fun refresh(): AuthResult {
        try {
            val json = "{\"refreshToken\":\"" + sessionManager.refreshToken?.value + "\"}"
            val response = client.post(Endpoint.REFRESH, json)!!
            if (response.statusCode() == 200) {
                val accessToken = itemJsonResponse(response, "accessToken")
                val validUntilString = itemJsonResponse(response, "validUntil")
                val validUntil: LocalDateTime = LocalDateTime.parse(validUntilString)
                return AuthResult(true, "Access acquired.", Token(accessToken, validUntil))
            } else {
                return AuthResult(false, "Access denied.")
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    // parsuje json z serwera
    fun itemJsonResponse(response: HttpResponse<String?>, item: String?): String {
        val jsonResponse = JsonParser.parseString(response.body()).asJsonObject
        return if (jsonResponse.has(item)) jsonResponse.get(item).asString else ""
    }
}