package tellem.repository

interface ProjectRepository {
    fun fetchProjectById(id: Int): String
    //fun getAllProjects(): String
    fun createProject(projectJson: String): Result<String>
    //fun updateProject(projectJson: String): String
    //fun deleteProject(id: Int): Boolean
}