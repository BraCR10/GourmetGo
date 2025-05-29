package gourmetgo.client.data.mockups

import gourmetgo.client.data.models.User

object UserMockup {

    private val testUser = User(
        id = "1",
        name = "Juan Pérez",
        email = "juan@test.com",
        phone = "88887777",
        role = "user",
        avatar = "https://via.placeholder.com/150",
        preferences = listOf("Italiana", "Vegana"),
        createdAt = "2024-01-15"
    )

    private val testChef = User(
        id = "2",
        name = "Chef María",
        email = "maria@chef.com",
        phone = "99998888",
        role = "chef",
        avatar = "https://via.placeholder.com/150",
        preferences = listOf("Fusión", "Gourmet"),
        createdAt = "2024-01-10"
    )

    val usersList = listOf(testUser, testChef)

    fun validateCredentials(email: String, password: String): User? {
        return when {
            email == "juan@test.com" && password == "123456" -> testUser
            email == "maria@chef.com" && password == "123456" -> testChef
            else -> null
        }
    }
}