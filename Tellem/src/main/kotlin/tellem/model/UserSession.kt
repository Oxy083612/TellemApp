package tellem.model

import tellem.model.Project

class UserSession {
    private var refreshToken: String = ""
    private var accessToken: String = ""
    private var uID: Int? = null
    private var projects = mutableListOf<Project>()

    fun addProject(project: Project) {
        this.projects.add(project)
    }

    fun getProject(pID: Int): Project {
        return Project("Uau", "mimi", 1)
    }

    fun removeProject(pID: Int) {
        this.projects.removeAt(pID)
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun getAccessToken(): String {
        return this.accessToken
    }

    fun setRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun getRefreshToken(): String {
        return this.refreshToken
    }

    fun setUserID(uID: Int) {
        this.uID = uID
    }

    fun getUserID(): Int? {
        return uID
    }

    fun reset() {
        this.accessToken = ""
        this.refreshToken = ""
        this.uID = null
        this.projects.clear()
    }

}