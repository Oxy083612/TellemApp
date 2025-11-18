package tellem.session

import tellem.launcher.service.AuthService
import tellem.util.Token
import java.time.LocalDateTime

class SessionManager {
    var refreshToken: Token? = null
    var accessToken: Token? = null
    //var uID: Int? = null

    fun setSession(refreshToken: Token, accessToken: Token) {
        this.refreshToken = refreshToken
        this.accessToken = accessToken
    }

    fun clearSession() {
        this.refreshToken = null
        this.accessToken = null
    }

    fun isLoggedIn(): Boolean {
        if(refreshToken == null || accessToken == null){
            return false
        }

        val now = LocalDateTime.now()
        val accessValid = accessToken?.validUntil?.isAfter(now) ?: false
        val refreshValid = refreshToken?.validUntil?.isAfter(now) ?: false

        return accessValid || refreshValid
    }

    fun isTokenExpired(): Boolean{
        val accessValid = accessToken?.validUntil?.isAfter(LocalDateTime.now()) ?: false
        return accessValid
    }

    fun refreshToken(authService: AuthService) {
        this.accessToken = authService.refresh().token
    }

}