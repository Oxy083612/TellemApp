package tellem.service

import com.google.gson.JsonParser
import tellem.api.HttpClient
import tellem.repository.http.HttpProjectRepository
import tellem.repository.ProjectResult
import tellem.repository.file.FileProjectRepository
import tellem.session.SessionManager

class ProjectService(val client: HttpClient, val sessionManager: SessionManager) {
    val httpProjectRepository = HttpProjectRepository(client, sessionManager)
    var fileProjectRepository = FileProjectRepository("users")

    fun fetchProject(pID: Int?): ProjectResult {
        return try {
            val result = httpProjectRepository.fetchProjectById(pID, sessionManager.uID)
            println(result.isSuccess)
            return result.fold(
                onSuccess = { body ->
                    val jsonObj = JsonParser.parseString(body).asJsonObject
                    val projectJsonObj = jsonObj.getAsJsonObject("project")
                    val projectId = projectJsonObj.get("id").asInt
                    val name = projectJsonObj.get("name")?.asString
                    val description = projectJsonObj.get("description").asString

                    ProjectResult(
                        status = true,
                        message = "Project created",
                        projectBody = body,
                        pID = projectId,
                        name = name,
                        description = description,
                    )
                },
                onFailure = {
                    ProjectResult(
                        status = false,
                        message = "Failed to create project: ${it.message}",
                        projectBody = "",
                        pID = null,
                        name = "",
                        description = ""
                    )
                })
        } catch (e: Exception){
            e.printStackTrace()
            ProjectResult(false, "Unexpected error", "", null, "", "")
        }
    }

    fun createProject(name: String, description: String): ProjectResult {
        return try {
            val json = "{\"name\":\"$name\", \"description\":\"$description\"}"
            val result = httpProjectRepository.createProject(json)

            return result.fold(
                onSuccess = { body ->
                    val jsonObj = JsonParser.parseString(body).asJsonObject
                    val projectJsonObj = jsonObj.getAsJsonObject("project")

                    val projectId = projectJsonObj.get("id")?.asInt
                    val name = projectJsonObj.get("name")?.asString
                    val description = projectJsonObj.get("description")?.asString
                    fileProjectRepository.createProject(body)

                    ProjectResult(
                        status = true,
                        message = "Project created",
                        projectBody = body,
                        pID = projectId,
                        name = name,
                        description = description
                    )
                },
                onFailure = {
                    ProjectResult(
                        status = false,
                        message = "Failed to create project: ${it.message}",
                        projectBody = "",
                        pID = null,
                        name = "",
                        description = ""
                    )
                }
            )

        } catch (e: Exception){
            e.printStackTrace()
            ProjectResult(false, "Unexpected error", "", null, "", "")
        }
    }
}