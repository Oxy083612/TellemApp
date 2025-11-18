package tellem.repository.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import tellem.model.Project
import tellem.repository.ProjectRepository
import java.io.File
import kotlin.js.ExperimentalJsFileName

class FileProjectRepository : ProjectRepository {
    var path: String? = "users"
    var file: File = File("users")
    var uID: Int? = null

    constructor(path: String?, uID: Int?){
        this.path = path
        this.uID = uID
    }

    constructor(): this("users", 10)

    override fun fetchProjectById(id: Int): String {
        return ""
    }

    override fun createProject(projectJson: String): Result<String> {
        return try {
            createUsersFolder()

            // Parsujemy JSON tylko po potrzebne pola
            val jsonObj = JsonParser.parseString(projectJson).asJsonObject
            val projectJsonObj = jsonObj.getAsJsonObject("project") // obiekt zagnieżdżony

            val name = projectJsonObj.get("name")?.asString ?: ""
            val description = projectJsonObj.get("description")?.asString ?: ""
            val pID = projectJsonObj.get("id")?.asInt ?: null

            // Tworzymy projekt z pustą listą taskLists
            val project = Project(name, description, pID)

            // Zapisujemy tylko wybrane pola do pliku
            val mapToSave = mapOf(
                "pID" to project.pID,
                "name" to project.name,
                "description" to project.description,
                "taskLists" to project.taskLists
            )

            File("$path/$uID.json").writeText(Gson().toJson(mapToSave))
            Result.success("OK")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun createUsersFolder() {
        if(!file.exists()){
            this.file.mkdirs()
        }
    }

    fun createDataFile() {

    }

    fun convertStringToData(dateString: String){

    }
}