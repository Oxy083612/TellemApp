package tellem.util

import tellem.app.SceneManager
import tellem.launcher.service.AuthService
import tellem.service.ProjectService
import tellem.session.SessionManager

interface Controllers {
    fun setSceneManager(sceneManager: SceneManager)

    fun setSessionManager(sessionManager: SessionManager)

    fun setAuthService(authService: AuthService)
}

interface AppController : Controllers {
    fun setProjectService(projectService: ProjectService)
}

interface LauncherController : Controllers