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

    private final val USERNAME_MIN_LENGTH: Int = 8
    private final val USERNAME_MAX_LENGTH: Int = 20
    private final val PASSWORD_MIN_LENGTH: Int = 7
    private final val PASSWORD_MAX_LENGTH: Int = 20
    //private final val EMAIL_REGEX: String = "(?:[a-z0-9!#\$%&'*+\\x2f=?^_`\\x7b-\\x7d~\\x2d]+(?:\\.[a-z0-9!#\$%&'*+\\x2f=?^_`\\x7b-\\x7d~\\x2d]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9\\x2d]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9\\x2d]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9\\x2d]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    fun login(username: String, password: String?): AuthResult {
        try {
            if (username.length !in USERNAME_MIN_LENGTH..USERNAME_MAX_LENGTH || password?.length !in PASSWORD_MIN_LENGTH .. PASSWORD_MAX_LENGTH){
                return AuthResult(
                    false,
                    -1,
                    "Login credentials are incorrect")
            }

            val json = "{\"username\":\"$username\", \"password\":\"$password\"}"

            val response = client.post(Endpoint.LOGIN, json)!!

            return if (response.statusCode() == 200) {
                val accessToken = itemJsonResponse(response, "accessToken")
                val accessTokenValidDateString = itemJsonResponse(response, "accessTokenValidDate")
                val accessTokenValidDate: LocalDateTime = LocalDateTime.parse(accessTokenValidDateString)

                val refreshToken = itemJsonResponse(response, "refreshToken")
                val refreshTokenValidDateString = itemJsonResponse(response, "refreshTokenValidDate")
                val refreshTokenValidDate: LocalDateTime = LocalDateTime.parse(refreshTokenValidDateString)

                val uID = itemJsonResponse(response, "uID").toInt()

                sessionManager.accessToken = Token(accessToken, accessTokenValidDate)
                sessionManager.refreshToken = Token(refreshToken, refreshTokenValidDate)
                sessionManager.uID = uID

                AuthResult(
                    true,
                    response.statusCode(),
                    itemJsonResponse(response, "message")
                )

            } else {
                AuthResult(
                    false,
                    response.statusCode(),
                    itemJsonResponse(response, "message")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(
                false,
                -1,
                "App error."
            )
        }
    }

    fun loginWithTokens(): AuthResult? {
        try {
            val json = "{\"accessToken\":\"${sessionManager.accessToken?.value}\"}"
            val response = client.post(Endpoint.LOGIN_TOKEN, json)!!
            if (response.statusCode() == 200) {
                return AuthResult(
                    true,
                    response.statusCode(),
                    "You logged in successfully."
                )
            } else if (response.statusCode() == 300) {
                // zmień kod na expired access token
                refresh()
                return loginWithTokens()
            } else {
                return AuthResult(
                    false,
                    response.statusCode(),
                    "Tokens expired")
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun register(username: String, password: String?, email: String?): AuthResult {
        try {
            val json = "{\"username\":\"$username\", \"password\":\"$password\", \"email\":\"$email\"}"

            val response = client.post(Endpoint.REGISTER, json)!!

            return if (response.statusCode() == 201) {
                AuthResult(
                    true,
                    response.statusCode(),
                    "Account is created. Please check your mail inbox for verification link")
            } else {
                AuthResult(
                    false,
                    response.statusCode(),
                    itemJsonResponse(response, "message")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(
                false,
                -1,
                "App error."
            )
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
                return AuthResult(
                    true,
                    response.statusCode(),
                    "Access acquired.",
                    Token(accessToken, validUntil)
                )
            } else {
                return AuthResult(
                    false,
                    response.statusCode(),
                    "Access denied."
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(
                false,
                -1,
                "App error."
            )
        }
    }

    fun resend(username: String): AuthResult {
        try {
            val json = "{\"username\":\"$username\"}"
            val response = client.post(Endpoint.RESEND, json)!!
            println(response.statusCode())
            return if (response.statusCode() == 200){
                AuthResult(
                    true,
                    response.statusCode(),
                    "Verification link was sent"
                )
            } else {
                AuthResult(
                    false,
                    response.statusCode(),
                    itemJsonResponse(response, "message")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AuthResult(
                false,
                -1,
                "App error."
            )
        }
    }

    // parsuje json z serwera
    fun itemJsonResponse(response: HttpResponse<String?>, item: String?): String {
        val jsonResponse = JsonParser.parseString(response.body()).asJsonObject
        return if (jsonResponse.has(item)) jsonResponse.get(item).asString else ""
    }
}