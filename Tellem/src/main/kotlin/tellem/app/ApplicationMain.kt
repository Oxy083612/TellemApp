package tellem.app

import eu.hansolo.fx.heatmap.Launcher
import javafx.application.Application
import javafx.stage.Stage
import tellem.api.HttpClient
import tellem.launcher.service.AuthService
import tellem.service.ProjectService
import tellem.session.SessionManager
import tellem.util.AppController
import tellem.util.LauncherController
import java.io.IOException

class ApplicationMain : Application() {

    @Throws(IOException::class)
    override fun start(stage: Stage) {
        //setting SceneManager and scene
        val sceneManager = SceneManager(stage)
        initializeModules(sceneManager)
        sceneManager.activate("launcher-view")
        stage.width = 800.0
        stage.height = 600.0
        stage.show()

    }

    fun initializeModules(sceneManager: SceneManager){
        val launcherController : LauncherController = sceneManager.runLoader("launcher-view")
        val appController : AppController = sceneManager.runLoader("app-view")

        //declare modules
        val client = HttpClient("http://localhost:3000")
        val sessionManager = SessionManager()
        val authService = AuthService(client, sessionManager)
        val projectService = ProjectService(client, sessionManager)

        //activate modules
        launcherController.setSceneManager(sceneManager)
        appController.setSceneManager(sceneManager)

        launcherController.setSessionManager(sessionManager)
        launcherController.setSessionManager(sessionManager)

        launcherController.setAuthService(authService)
        appController.setAuthService(authService)

        appController.setProjectService(projectService)
    }

    @Throws(Exception::class)
    override fun stop() {
        //TellemController.saveDataBeforeExit();
    }

}