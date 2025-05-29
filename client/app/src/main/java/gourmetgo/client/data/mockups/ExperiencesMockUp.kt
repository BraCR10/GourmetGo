package gourmetgo.client.data.mockups

import gourmetgo.client.data.models.Experience

object ExperiencesMockup {

    private val experiencesList = listOf(
        Experience(
            id = "1",
            title = "Taller de Sushi para Principiantes",
            description = "Aprende a preparar sushi tradicional japonés con ingredientes frescos y técnicas auténticas. Incluye degustación y certificado de participación.",
            date = "2025-06-15T19:00:00Z",
            location = "San José Centro, Costa Rica",
            capacity = 15,
            remainingCapacity = 8,
            price = 25000.0,
            duration = 3.0,
            category = "Taller",
            images = listOf(
                "https://images.unsplash.com/photo-1579952363873-27d3bfad9c0d?w=400",
                "https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=400"
            ),
            requirements = "Traer delantal y muchas ganas de aprender",
            status = "Activa",
            chef = "chef1",
            createdAt = "2025-01-15T10:00:00Z"
        ),
        Experience(
            id = "2",
            title = "Cena Italiana Gourmet",
            description = "Una experiencia culinaria italiana auténtica con platos tradicionales de la Toscana. Menú de 5 tiempos con maridaje de vinos.",
            date = "2025-06-20T20:00:00Z",
            location = "Escazú, San José",
            capacity = 20,
            remainingCapacity = 5,
            price = 35000.0,
            duration = 2.5,
            category = "Cena",
            images = listOf(
                "https://images.unsplash.com/photo-1551782450-17144efb9c50?w=400",
                "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400"
            ),
            requirements = "Código de vestimenta: Casual elegante",
            status = "Activa",
            chef = "chef2",
            createdAt = "2025-01-18T14:30:00Z"
        ),
        Experience(
            id = "3",
            title = "Cocina Mediterránea: Tapas Españolas",
            description = "Descubre los sabores auténticos de España preparando una variedad de tapas tradicionales con ingredientes importados.",
            date = "2025-06-25T18:30:00Z",
            location = "Heredia Centro",
            capacity = 12,
            remainingCapacity = 12,
            price = 28000.0,
            duration = 3.5,
            category = "Taller",
            images = listOf(
                "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=400"
            ),
            requirements = "Nivel principiante a intermedio",
            status = "Próximamente",
            chef = "chef3",
            createdAt = "2025-01-20T09:15:00Z"
        ),
        Experience(
            id = "4",
            title = "Brunch Vegano Gourmet",
            description = "Un brunch completamente vegano con opciones creativas y nutritivas. Incluye batidos verdes, pancakes de avena y más.",
            date = "2025-06-10T10:00:00Z",
            location = "Santa Ana, San José",
            capacity = 16,
            remainingCapacity = 0,
            price = 22000.0,
            duration = 2.0,
            category = "Brunch",
            images = listOf(
                "https://images.unsplash.com/photo-1511690743698-d9d85f2fbf38?w=400",
                "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=400"
            ),
            requirements = "Apto para vegetarianos y veganos",
            status = "Agotada",
            chef = "chef4",
            createdAt = "2025-01-12T16:45:00Z"
        ),
        Experience(
            id = "5",
            title = "Repostería Francesa: Macarons y Éclairs",
            description = "Aprende las técnicas clásicas de la repostería francesa para crear macarons perfectos y éclairs deliciosos.",
            date = "2025-07-05T15:00:00Z",
            location = "Cartago Centro",
            capacity = 10,
            remainingCapacity = 7,
            price = 32000.0,
            duration = 4.0,
            category = "Repostería",
            images = listOf(
                "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400"
            ),
            requirements = "Experiencia básica en repostería recomendada",
            status = "Activa",
            chef = "chef5",
            createdAt = "2025-01-25T11:20:00Z"
        ),
        Experience(
            id = "6",
            title = "Asado Argentino Tradicional",
            description = "Una experiencia completa de asado argentino con cortes premium, chimichurri casero y vinos malbec.",
            date = "2025-06-30T17:00:00Z",
            location = "Alajuela Centro",
            capacity = 25,
            remainingCapacity = 18,
            price = 45000.0,
            duration = 4.0,
            category = "Asado",
            images = listOf(
                "https://images.unsplash.com/photo-1529692236671-f1f6cf9683ba?w=400",
                "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400"
            ),
            requirements = "Apto para todos los niveles",
            status = "Activa",
            chef = "chef6",
            createdAt = "2025-01-28T13:10:00Z"
        )
    )

    fun getAllExperiences(): List<Experience> {
        return experiencesList
    }

    fun getActiveExperiences(): List<Experience> {
        return experiencesList.filter { it.status == "Activa" }
    }

    fun getExperienceById(id: String): Experience? {
        return experiencesList.find { it.id == id }
    }

    fun getExperiencesByCategory(category: String): List<Experience> {
        return experiencesList.filter {
            it.category.equals(category, ignoreCase = true)
        }
    }

    fun searchExperiences(query: String): List<Experience> {
        return experiencesList.filter { experience ->
            experience.title.contains(query, ignoreCase = true) ||
                    experience.description.contains(query, ignoreCase = true) ||
                    experience.category.contains(query, ignoreCase = true) ||
                    experience.location.contains(query, ignoreCase = true)
        }
    }

    fun getAvailableCategories(): List<String> {
        return experiencesList.map { it.category }.distinct().sorted()
    }

    // Simular filtros adicionales
    fun getExperiencesByPriceRange(minPrice: Double, maxPrice: Double): List<Experience> {
        return experiencesList.filter { it.price in minPrice..maxPrice }
    }

    fun getExperiencesByDuration(minHours: Double, maxHours: Double): List<Experience> {
        return experiencesList.filter { it.duration in minHours..maxHours }
    }

    fun getPopularExperiences(): List<Experience> {
        // Simular experiencias populares (las que tienen menos capacidad restante)
        return experiencesList
            .filter { it.status == "Activa" }
            .sortedBy { it.remainingCapacity.toDouble() / it.capacity }
            .take(3)
    }

    fun getUpcomingExperiences(): List<Experience> {
        // Simular experiencias próximas (ordenadas por fecha)
        return experiencesList
            .filter { it.status == "Activa" || it.status == "Próximamente" }
            .sortedBy { it.date }
    }
}