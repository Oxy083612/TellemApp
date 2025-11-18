package tellem.model

import java.time.LocalDate

class TaskList {
    var name: String?
    var tasks: MutableList<Task?> = ArrayList()

    constructor() {
        name = "Task list"
    }

    constructor(nameTmp: String?) {
        name = nameTmp
    }

    fun addTask(task: Task?) {
        tasks.add(task)
    }

    fun addTask(name: String?, desc: String?, date1: LocalDate?, date2: LocalDate?) {
        tasks.add(Task(name, desc, date1, date2))
    }

    fun addTask(name: String?, date1: LocalDate?, date2: LocalDate?) {
        tasks.add(Task(name, date1, date2))
    }

    fun addTask(name: String?, desc: String?, date1: LocalDate?) {
        tasks.add(Task(name, desc, date1))
    }

    fun addTask(name: String?, date1: LocalDate?) {
        tasks.add(Task(name, date1))
    }

    fun removeTask(id: Int) {
        tasks[id] = null
        tasks.removeAt(id)
    }
}