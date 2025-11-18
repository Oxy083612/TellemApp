package tellem.model

import java.time.LocalDate

class Task {
    //setters
    //getters
    var name: String?
    var desc: String?
    var added: LocalDate?
    var isItDue: Boolean
        private set
    private var due: LocalDate?

    constructor(nameTmp: String?, descTmp: String?, date1: LocalDate?, date2: LocalDate?) {
        this.name = nameTmp
        this.desc = descTmp
        this.added = date1
        this.due = date2
        this.isItDue = true
    }

    constructor(nameTmp: String?, date1: LocalDate?, date2: LocalDate?) {
        this.name = nameTmp
        this.desc = ""
        this.added = date1
        this.due = date2
        this.isItDue = true
    }

    constructor(nameTmp: String?, descTmp: String?, date1: LocalDate?) {
        this.name = nameTmp
        this.desc = descTmp
        this.added = date1
        this.isItDue = false
        this.due = null
    }

    constructor(nameTmp: String?, date1: LocalDate?) {
        this.name = nameTmp
        this.desc = ""
        this.added = date1
        this.isItDue = false
        this.due = null
    }

    fun setDue(date: LocalDate?) {
        if (date == null) {
            isItDue = false
        } else {
            isItDue = true
            due = date
        }
    }

    fun getDue(): LocalDate? {
        return due
    }
}