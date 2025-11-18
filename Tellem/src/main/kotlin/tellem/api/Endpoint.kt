package tellem.api

enum class Endpoint(val path: String) {
    LOGIN("/login"),
    LOGIN_TOKEN("/loginToken"),
    REGISTER("/register"),
    REFRESH("/refresh"),

    PROJECT("/project"),
}