package tellem.launcher.ui

import tellem.app.SceneManager
import tellem.launcher.service.AuthService
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tellem.session.SessionManager
import tellem.util.LauncherController

class LauncherController : LauncherController {
    //@FXML
    //private lateinit var blackLine: VBox

    @FXML
    private lateinit var communicationMenu: VBox

    @FXML
    private lateinit var errorLabelL: Label

    @FXML
    private lateinit var errorLabelR: Label

    //@FXML
    //private lateinit var functionalContainer: StackPane

    //@FXML
    //private lateinit var leftPanel: VBox

    //@FXML
    //private lateinit var loginField: VBox

    @FXML
    private lateinit var loginMenu: VBox

    @FXML
    private lateinit var loginL: TextField

    @FXML
    private lateinit var passwordL: PasswordField

    @FXML
    private lateinit var mainMenu: VBox

    @FXML
    private lateinit var registerMenu: VBox

    @FXML
    private lateinit var loginR: TextField

    @FXML
    private lateinit var passwordR: PasswordField

    @FXML
    private lateinit var passwordConfirmR: PasswordField

    @FXML
    private lateinit var emailR: TextField

    //@FXML
    //private lateinit var rightPanel: VBox

    //@FXML
    //private lateinit var rootPane: StackPane

    //@FXML
    //private lateinit var info: Text

    private lateinit var sceneManager: SceneManager
    private lateinit var sessionManager: SessionManager
    private lateinit var authService : AuthService

    @FXML
    fun exit() {
        val stage = loginMenu.scene.window as Stage
        stage.close()
    }


    @Suppress("unused")
    @FXML
    private fun onLogInClick() {
        if (sessionManager.isLoggedIn()) {
            val result = authService.loginWithTokens()
            if (result!!.status) {                  // sprawdź access token
                changeViewToApp()
            } else {
                val refreshResult = authService.refresh() //odnów access token
                if (refreshResult.status) {              //jeśli jest ważny zaloguj
                    changeViewToApp()
                } else {
                    mainMenu.isVisible = false
                    loginMenu.isVisible = true
                }
            }
        } else {
            mainMenu.isVisible = false
            loginMenu.isVisible = true
        }
    }

    @Suppress("unused")
    @FXML
    private fun onLoggingInClick() {
        try {
            hideErrors()
            val result = authService.login(loginL.text, passwordL.text)
            if (result.status) {
                changeViewToApp()
            } else {
                errorLabelL.text = result.message
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorLabelL.text = "Unidentified error"
        }
    }

    @FXML
    fun onRegisterClick() {
        mainMenu.isVisible = false
        registerMenu.isVisible = true
    }

    @FXML
    fun onRegisterInClick() {
        hideErrors()
        val login = loginR.text
        val pass = passwordR.text
        val passConfirm = passwordConfirmR.text
        val email = emailR.text

        if (login.isEmpty() || pass.isEmpty() || passConfirm.isEmpty() || email.isEmpty()) {
            errorLabelR.text = "No field cannot be empty."
            passwordR.text = ""
            passwordConfirmR.text = ""
        } else if (pass != passConfirm) {
            errorLabelR.text = "Password and confirm password fields are not matching."
            passwordR.text = ""
            passwordConfirmR.text = ""
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$".toRegex())) {
            errorLabelR.text = "Wrong e-mail format."
            passwordR.text = ""
            passwordConfirmR.text = ""
        } else if (!pass.matches("^(?=.*[A-Z]).{8,}$".toRegex())) {
            errorLabelR.text = "Password must contain at least one big case letter and be at least 8 characters long."
            passwordR.text = ""
            passwordConfirmR.text = ""
        } else if (!login.matches("^.{8,}$".toRegex())) {
            errorLabelR.text = "Username has to be at least 8 characters long."
            passwordR.text = ""
            passwordConfirmR.text = ""
        } else {
            try {
                hideErrors()

                val result = authService.register(login, pass, email)
                errorLabelR.text = result.message
            } catch (e: Exception) {
                e.printStackTrace()
                errorLabelR.text = "Unidentified error"
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onReturnClick() {
        mainMenu.isVisible = true
        loginMenu.isVisible = false
        registerMenu.isVisible = false
        communicationMenu.isVisible = false
        hideErrors()

        loginL.text = ""
        passwordL.text = ""

        loginR.text = ""
        passwordR.text = ""
        passwordConfirmR.text = ""
        emailR.text = ""
    }

    @FXML
    fun onOptionsClick() {
    }

    override fun setSceneManager(sceneManager: SceneManager) {
        this.sceneManager = sceneManager
    }

    override fun setSessionManager(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    override fun setAuthService(authService: AuthService) {
        this.authService = authService
    }

    fun changeViewToApp() {
        sceneManager.activate("app-view")
    }

    private fun hideErrors() {
        errorLabelL.text = ""
        errorLabelR.text = ""
    }

    /*
    public void handleResponse(int status, JsonObject response, String endpoint, String method){
        if (status >= 200 && status < 300){

            if (endpoint.equals("/register") && method.equals("POST")){
                System.out.println("Status: " + status + "\n" + response);
                hideErrors();
                registerMenu.setVisible(false);
                communicationMenu.setVisible(true);
                info.setText("Account created successfully. Please Verify your account using link sent in your e-mail inbox before logging in.");
            }

            if (endpoint.equals("/login") && method.equals("POST")){
                System.out.println("Status: " + status + "\n" + response);

                this.accessToken = String.valueOf(response.get("access"));
                this.refreshToken = String.valueOf(response.get("refresh"));

                System.out.println(accessToken + "\n" + refreshToken);

                hideErrors();

                // should change view to proper app
                loginMenu.setVisible(false);
                communicationMenu.setVisible(true);
                info.setText("You logged in successfully");


            }

        } else if ( status > 300 ){

            hideErrors();

            if (endpoint.equals("/register")){
                errorLabelR.setText(String.valueOf(response));
            }

            System.out.println("Status: " + status + "\n" + response);

        }
    }

 */
}