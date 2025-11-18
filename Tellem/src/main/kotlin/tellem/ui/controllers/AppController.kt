package tellem.ui.controllers

import javafx.fxml.FXML
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
    private fun onAddProject() {
        createProjectMenu.isVisible = true
    }

    @FXML
    private fun onReturnToMenu() {
        createProjectMenu.isVisible = false
    }

    @FXML
    private fun onCreateProject() {
        try {
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
                        readProject();
                    } else {
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

    private fun readProject(){
        println("Coś stworzone")
    }

    private fun hideErrors(){
        createProjectError.isVisible = false
    }
}