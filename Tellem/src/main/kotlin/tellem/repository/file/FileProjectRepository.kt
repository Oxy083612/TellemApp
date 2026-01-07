package tellem.repository.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import tellem.model.Project
import tellem.repository.ProjectRepository
import java.io.File
import java.io.FileReader

class FileProjectRepository(private val path: String) : ProjectRepository {

    override fun fetchProjectById(pID: Int?, uID: Int?): Result<String> {
        try {
            val file = File("$path/$uID.json")
            if (!file.exists()){
                return Result.failure(Exception("File couldn't be found"));
            }

            val jsonContent = file.readText();

            val rootObj = JsonParser.parseString(jsonContent).asJsonObject

            val projectsArray = rootObj.getAsJsonArray("projects");

            val found = projectsArray.firstOrNull() { element ->
                val obj = element.asJsonObject
                obj.get("id").asInt == pID
            }

            println(found?.toString())

            return Result.success("OK")
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override fun createProject(projectJson: String): Result<String> {
        return try {
            createUsersFolder()

            val jsonObj = JsonParser.parseString(projectJson).asJsonObject
            val projectJsonObj = jsonObj.getAsJsonObject("project")


            val uID = projectJsonObj.get("user_id").asInt
            val name = projectJsonObj.get("name").asString
            val description = projectJsonObj.get("description").asString
            val pID = projectJsonObj.get("id").asInt

            val project = Project(name, description, pID)
            val file = File("$path/$uID.json")

            val gson = Gson()

            val rootObj = if (file.exists()) {
                val parsed = JsonParser.parseString(file.readText()).asJsonObject

                if (!parsed.has("projects")) {
                    parsed.add("projects", gson.toJsonTree(mutableListOf<Project>()))
                }

                parsed
            } else {
                JsonParser.parseString("""{"projects": []}""").asJsonObject
            }

            val projectsArray = rootObj.getAsJsonArray("projects")

            val exists = projectsArray.any { it.asJsonObject.get("id").asInt == pID }
            if (exists) return Result.failure(Exception("Project already exists"))

            val projectToSave = mapOf(
                "id" to pID,
                "name" to name,
                "description" to description,
                "taskLists" to emptyList<Any>() // lub project.taskLists jeśli nie zawiera dat
            )

            this.fetchProjectById(pID, uID)

            projectsArray.add(gson.toJsonTree(projectToSave))

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            file.writeText(gsonPretty.toJson(rootObj))

            Result.success("OK")

        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun createUsersFolder() {
        if(!File(path).exists()){
            File(path).mkdirs()
        }
    }

    fun createDataFile() {

    }

    fun convertStringToData(dateString: String){

    }
}