package tellem.api

enum class Endpoint(val path: String) {
    LOGIN("/login"),
    LOGIN_TOKEN("/loginToken"),
    REGISTER("/register"),
    REFRESH("/refresh"),
    RESEND("/resend"),
    PROJECT("/project");

    fun addQuery(key: String, value: String): String {
        return this.path + "?$key=$value"
    }
}