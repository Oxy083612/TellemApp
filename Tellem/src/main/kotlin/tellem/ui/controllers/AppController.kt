package tellem.ui.controllers

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import tellem.app.SceneManager
import tellem.launcher.service.AuthService
import tellem.repository.file.FileProjectRepository
import tellem.service.ProjectService
import tellem.session.SessionManager
import tellem.util.AppController

class AppController : AppController {
    private lateinit var sceneManager: SceneManager
    private lateinit var sessionManager: SessionManager
    private lateinit var authService: AuthService
    private lateinit var projectService: ProjectService

    override fun setSceneManager(sceneManager: SceneManager) {
        this.sceneManager = sceneManager
    }

    override fun setSessionManager(sessionManager: SessionManager){
        this.sessionManager = sessionManager
    }

    override fun setAuthService(authService: AuthService) {
        this.authService = authService
    }

    override fun setProjectService(projectService: ProjectService) {
        this.projectService = projectService
    }

    @FXML
    private lateinit var createProjectMenu: VBox

    @FXML
    private lateinit var readProjectMenu: VBox

    @FXML
    private lateinit var createProjectError: Label

    @FXML
    private lateinit var createProjectName: TextField

    @FXML
    private lateinit var createProjectDesc: TextField

    @FXML
    private lateinit var projectList: VBox

    @FXML
    private lateinit var projectDescription: Label

    @FXML
    private lateinit var projectName: Label

    @FXML
    private lateinit var createTaskListButton: Button

    @FXML
    private lateinit var taskListList: HBox

    @FXML
    private fun onAddProject() {
        createProjectMenu.isVisible = true
        readProjectMenu.isVisible = false
    }

    @FXML
    private fun onReturnToMenu() {
        createProjectMenu.isVisible = false
        readProjectMenu.isVisible = false
    }

    @FXML
    private fun onCreateProject() {
        try {
            readProjectMenu.isVisible = false
            hideErrors()
            val name = createProjectName.text
            val desc = createProjectDesc.text

            if(name.isEmpty()){
                createProjectError.text = "Name field cannot be empty."
                createProjectName.text = ""
                createProjectError.text = ""
            } else {
                try {
                    val projectResult = projectService.createProject(name, desc)
                    println("STATUS: " + projectResult.status + "\nMESSAGE: " + projectResult.message)
                    if (projectResult.status){
                        readProject(projectResult.pID)
                        addProjectToList(projectResult.name, projectResult.pID)
                    } else {
                        createProjectError.isVisible = true
                        createProjectError.text = projectResult.message
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    createProjectError.text = "Unidentified error"
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        onReturnToMenu()
    }

    private fun readProject(pID: Int?){
        hideErrors()
        createProjectMenu.isVisible = false
        readProjectMenu.isVisible = true
        val result = projectService.fetchProject(pID);
        if (result.status){
            projectName.text = result.name
            projectDescription.text = result.description
        }
    }

    private fun hideErrors(){
        createProjectError.isVisible = false
    }

    private fun addProjectToList(name: String?, pID: Int?) {
        var button = Button()
        button.text = name
        println(name)
        projectList.children.addFirst(button)
        button.setOnMouseClicked {
            readProject(pID)
        }
    }

    @FXML
    private fun onLogoutClick() {
        changeViewToLauncher()
        sessionManager.clearSession()
    }

    private fun changeViewToLauncher() {
        sceneManager.activate("launcher-view")
    }

    @FXML
    private fun onCreateTaskListClick(){
        taskListList.children.remove(createTaskListButton)

        val newTaskList = VBox()

        val taskListNameLabel = Label()
        taskListNameLabel.text = "Name"
        taskListNameLabel.style = "-fx-font-size: 19px; -fx-font-weight: bold;"

        val taskListName = TextField()
        taskListName.promptText = "Name"
        taskListName.id = "taskListName"

        val taskListDescriptionLabel = Label()
        taskListDescriptionLabel.text = "Description"
        taskListDescriptionLabel.style = "-fx-font-size: 19px; -fx-font-weight: bold;"

        val taskListDescription = TextField()
        taskListDescription.promptText = "Description"
        taskListDescription.id = "taskListDescription"

        val taskListReturnButton = Button()

        val taskListConfirmButton = Button()

        val buttonBox = VBox()
        buttonBox.children.add(taskListConfirmButton)
        buttonBox.children.add(taskListReturnButton)

        taskListList.children.add(newTaskList)
        newTaskList.children.add(taskListNameLabel)
        newTaskList.children.add(taskListName)
        newTaskList.children.add(taskListDescriptionLabel)
        newTaskList.children.add(taskListDescription)
        newTaskList.children.add(buttonBox)

        taskListReturnButton.text = "Return"
        taskListReturnButton.setOnAction {
            taskListList.children.remove(newTaskList)
            if(!taskListList.children.contains(createTaskListButton)){
                taskListList.children.add(createTaskListButton)
            }
        }

        taskListConfirmButton.text = "Confirm"
        taskListConfirmButton.setOnAction {
            onTaskListConfirm(taskListName.text, taskListDescription.text, newTaskList)
            val nameIndex = newTaskList.children.indexOf(taskListName)
            val descIndex = newTaskList.children.indexOf(taskListDescription)
            newTaskList.children.remove(buttonBox)
            newTaskList.children.remove(taskListName)
            newTaskList.children.remove(taskListDescription)
            val listName = Label()
            val listDesc = Label()
            listName.text = taskListName.text
            listDesc.text = taskListDescription.text
            newTaskList.children.add(nameIndex, listName)
            newTaskList.children.add(descIndex, listDesc)
            if(!taskListList.children.contains(createTaskListButton)){
                taskListList.children.add(createTaskListButton)
            }
        }
    }

    @FXML
    private fun onTaskListConfirm(taskListName: String, taskListDescription: String, taskList: VBox){
        var addTaskButton = Button()
        addTaskButton.text = "Add Task"
        addTaskButton.setOnAction {
            onAddTask()
        }
        taskList.children.add(addTaskButton)


        var removeTaskListButton = Button()
        removeTaskListButton.text = "Remove"
        removeTaskListButton.setOnAction {
            taskListList.children.remove(taskList)
        }
        taskList.children.add(removeTaskListButton)
    }

    private fun onAddTask() {

    }
}