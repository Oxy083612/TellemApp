package tellem.repository

interface ProjectRepository {
    fun fetchProjectById(pID: Int, uID: Int?): Result<String>
    //fun getAllProjects(): String
    fun createProject(projectJson: String): Result<String>
    //fun updateProject(projectJson: String): String
    //fun deleteProject(id: Int): Boolean
}