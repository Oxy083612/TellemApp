package tellem.repository.http

import tellem.api.Endpoint
import tellem.api.HttpClient
import tellem.repository.ProjectRepository
import tellem.session.SessionManager

class HttpProjectRepository(private val client: HttpClient, private val sessionManager: SessionManager) :
    ProjectRepository {

    override fun fetchProjectById(pID: Int?, uID: Int?): Result<String> {
        return Result.success("OK")
    }

    override fun createProject(projectJson: String): Result<String> {
        try {
            val token = sessionManager.accessToken?.value!!
            val response = client.post(Endpoint.PROJECT, projectJson, token)

            if (response != null && response.statusCode() == 200){
                return Result.success(response.body() ?: "")
            } else {
                return Result.failure(Exception("HTTP ${response?.statusCode()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}