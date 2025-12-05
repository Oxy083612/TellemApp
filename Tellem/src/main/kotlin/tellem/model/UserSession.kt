package tellem.model

import tellem.model.Project

class UserSession(private var uID: Int? = null) {

    fun setUserID(uID: Int) {
        this.uID = uID
    }

    fun getUserID(): Int? {
        return uID
    }

    fun reset() {
        this.uID = null
    }

}