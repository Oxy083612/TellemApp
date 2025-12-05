package tellem.ui.controllers

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tellem.app.SceneManager
import tellem.launcher.service.AuthService
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
}