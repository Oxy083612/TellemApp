package tellem.service

import com.google.gson.JsonParser
import tellem.api.HttpClient
import tellem.repository.http.HttpProjectRepository
import tellem.repository.ProjectResult
import tellem.repository.file.FileProjectRepository
import tellem.session.SessionManager

class ProjectService(client: HttpClient, sessionManager: SessionManager) {
    val httpProjectRepository = HttpProjectRepository(client, sessionManager)
    val fileProjectRepository = FileProjectRepository()

    fun fetchProject(pID: Int) {

    }

    fun createProject(name: String, description: String): ProjectResult {
        return try {
            val json = "{\"name\":\"$name\", \"description\":\"$description\"}"
            val result = httpProjectRepository.createProject(json)


            return result.fold(
                onSuccess = { body ->
                    val json = JsonParser.parseString(body).asJsonObject
                    val projectId = json.get("pID")?.asInt

                    fileProjectRepository.createProject(body)

                    ProjectResult(
                        status = true,
                        message = "Project created",
                        projectBody = body,
                        pID = projectId
                    )
                },
                onFailure = {
                    ProjectResult(
                        status = false,
                        message = "Failed to create project: ${it.message}",
                        projectBody = "",
                        pID = null
                    )
                }
            )

        } catch (e: Exception){
            e.printStackTrace()
            ProjectResult(false, "Unexpected error", "", null)
        }
    }
}