package tellem.app

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import tellem.util.Controllers

class SceneManager(val stage: Stage) {
    private val screenMap = HashMap<String, Scene>()

    fun addScreen(name: String, scene: Scene) {
        screenMap[name] = scene
    }

    fun removeScreen(name: String) {
        screenMap.remove(name)
    }

    fun <T> runLoader(name: String) : T {
        val loader = FXMLLoader(javaClass.getResource("$name.fxml"))

        val root = loader.load<Parent>()
        val scene = Scene(root)

        val controller = loader.getController<T>()

        addScreen(name, scene)

        return controller
    }

    fun activate(name: String) {
        val p: Scene? = screenMap.get(name)
        if (p == null) {
            System.err.println("ScreenController: brak ekranu o nazwie '$name'")
            return
        }
        stage.scene = p
    }
}