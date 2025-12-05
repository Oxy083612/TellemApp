package tellem.launcher.service

import tellem.util.Token

class AuthResult(val status: Boolean, val code: Int, val message: String?, val token: Token? = null)