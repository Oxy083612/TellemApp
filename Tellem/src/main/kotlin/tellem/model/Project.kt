package tellem.model

import tellem.model.TaskList

class Project(var name: String, var description: String, var pID: Int?) {
    var taskLists: MutableList<TaskList> = ArrayList()

    fun addTaskList(taskList: TaskList) {
        this.taskLists.add(taskList)
    }

    fun removeTaskList(id: Int) {
        this.taskLists.removeAt(id)
    }

    fun taskListSize(): Int {
        return taskLists.size
    }
}