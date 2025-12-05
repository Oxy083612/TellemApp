package tellem.service

import com.google.gson.JsonParser
import tellem.api.HttpClient
import tellem.repository.http.HttpProjectRepository
import tellem.repository.ProjectResult
import tellem.repository.file.FileProjectRepository
import tellem.session.SessionManager

class ProjectService(val client: HttpClient, val sessionManager: SessionManager) {
    val httpProjectRepository = HttpProjectRepository(client, sessionManager)
    var fileProjectRepository = FileProjectRepository("users", 0)

    fun fetchProject(pID: Int) {

    }

    fun createProject(name: String, description: String): ProjectResult {
        return try {
            fileProjectRepository = FileProjectRepository("users", sessionManager.uID)

            val json = "{\"name\":\"$name\", \"description\":\"$description\"}"
            val result = httpProjectRepository.createProject(json)


            return result.fold(
                onSuccess = { body ->
                    val jsonObj = JsonParser.parseString(body).asJsonObject
                    val projectJsonObj = jsonObj.getAsJsonObject("project")

                    val projectId = projectJsonObj.get("id")?.asInt
                    val name = projectJsonObj.get("name")?.asString
                    fileProjectRepository.createProject(body)

                    println("ID: $projectId + $name")

                    ProjectResult(
                        status = true,
                        message = "Project created",
                        projectBody = body,
                        pID = projectId,
                        name = name
                    )
                },
                onFailure = {
                    ProjectResult(
                        status = false,
                        message = "Failed to create project: ${it.message}",
                        projectBody = "",
                        pID = null,
                        name = ""
                    )
                }
            )

        } catch (e: Exception){
            e.printStackTrace()
            ProjectResult(false, "Unexpected error", "", null, "")
        }
    }
}