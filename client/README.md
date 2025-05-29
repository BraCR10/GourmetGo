# GourmetGo Client - Android Architecture Deep Dive

## MVVM Architecture Implementation

### Why MVVM in Android?
MVVM (Model-View-ViewModel) is the recommended architecture pattern for Android apps because:
- **Lifecycle Awareness**: ViewModels survive configuration changes (screen rotations)
- **Separation of Concerns**: Clear separation between UI and business logic
- **Testability**: ViewModels can be unit tested without Android dependencies
- **Data Binding**: UI automatically updates when data changes

---

## Core Components Explanation

### 1. **ViewModels** - The Heart of MVVM

#### `AuthViewModel.kt`
```kotlin
class AuthViewModel(context: Context) : ViewModel() {
    private val repository = AuthRepository(context)
    var uiState by mutableStateOf(AuthUiState())
    
    fun login(email: String, password: String) {
        // Handles login logic and updates UI state
    }
}
```

**Purpose:**
- Manages authentication state (login/logout)
- Handles user input validation
- Communicates with AuthRepository
- Survives configuration changes
- Updates UI through state management

#### `ExperiencesViewModel.kt`
```kotlin
class ExperiencesViewModel(context: Context) : ViewModel() {
    private val repository = ExperiencesRepository(context)
    var uiState by mutableStateOf(ExperiencesUiState())
    
    fun searchExperiences(query: String) { /* ... */ }
    fun filterByCategory(category: String?) { /* ... */ }
}
```

**Purpose:**
- Manages experiences list state
- Handles search and filtering logic
- Manages loading states
- Handles error states

---

### 2. **UI States** - Managing Screen State

#### `AuthUiState.kt`
```kotlin
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
```

**Why UI States?**
- **Single Source of Truth**: All UI state in one place
- **Immutable**: State changes are predictable
- **Reactive UI**: Compose automatically recomposes when state changes
- **Easy Debugging**: Clear state representation

#### `ExperiencesUiState.kt`
```kotlin
data class ExperiencesUiState(
    val isLoading: Boolean = false,
    val experiences: List<Experience> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val error: String? = null
)
```

---

### 3. **Factories** - Dependency Injection Pattern

#### Why Factories?
ViewModels need dependencies (like Context for repositories), but ViewModels are created by the Android system. Factories solve this dependency injection problem.

#### `AuthViewModelFactory.kt`
```kotlin
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

**Usage in Composable:**
```kotlin
@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context) // Inject context dependency
    )
}
```

**Benefits:**
- ✅ Proper dependency injection
- ✅ ViewModels get required dependencies
- ✅ Testable (can inject mock dependencies)
- ✅ Follows Android architecture guidelines

---

### 4. **Repository Pattern** - Data Abstraction

#### `AuthRepository.kt`
```kotlin
class AuthRepository(context: Context) {
    private val connection = Connection()
    private val sharedPrefs = SharedPrefsManager(context)
    
    suspend fun login(email: String, password: String): Result<User> {
        return if (AppConfig.USE_MOCKUP) {
            loginWithMockup(email, password)
        } else {
            loginWithApi(email, password)
        }
    }
}
```

**Purpose:**
- **Data Abstraction**: Hides data source details from ViewModels
- **Single Responsibility**: Only handles data operations
- **Testability**: Can be easily mocked for testing
- **Flexibility**: Can switch between API and mock data

---

### 5. **Data Layer Architecture**

#### **Models** (`data/models/`)
```kotlin
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "user"
)

data class Experience(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0
)
```

#### **DTOs** (`data/models/dtos/`)
Data Transfer Objects for API communication:
```kotlin
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val user: User)
```

#### **Network Layer** (`data/remote/`)
```kotlin
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @GET("experiences")
    suspend fun getExperiences(): ExperiencesResponse
}
```

#### **Local Storage** (`data/localStorage/`)
```kotlin
class SharedPrefsManager(context: Context) {
    fun saveToken(token: String) { /* ... */ }
    fun getToken(): String? { /* ... */ }
    fun saveUser(user: User) { /* ... */ }
}
```

---

### 6. **UI Components Structure**

#### **Screens** (`ui/screens/`)
Full-screen composables that use ViewModels:
```kotlin
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState = viewModel.uiState
    // UI implementation
}
```

#### **Components** (`ui/components/`)
Reusable UI components:
```kotlin
@Composable
fun ExperienceCard(
    experience: Experience,
    onBookClick: () -> Unit
) {
    Card {
        // Experience card UI
    }
}
```

#### **Navigation** (`ui/navigation/`)
```kotlin
@Composable
fun MainNavigation() {
    NavHost(/* navigation setup */) {
        composable("login") { LoginScreen() }
        composable("experiences") { ExperiencesScreen() }
    }
}
```

---

### 7. **Configuration & Utils**

#### `Config.kt` - App Configuration
```kotlin
object AppConfig {
    const val USE_MOCKUP = true
    const val API_BASE_URL = "http://localhost:3000/api/"
    const val ENABLE_LOGGING = true
    const val MOCK_NETWORK_DELAY = 800L
}
```

**Purpose:**
- Environment-specific settings
- Feature flags (mockup vs real API)
- Easy configuration changes

#### **Mockups** (`data/mockups/`)
For development and testing:
```kotlin
object UserMockup {
    private val testUsers = listOf(/* mock users */)
    fun validateCredentials(email: String, password: String): User?
}
```

---

## Data Flow in MVVM

```
┌─────────────┐    ┌──────────────┐    ┌─────────────┐
│    View     │    │  ViewModel   │    │    Model    │
│ (Composable)│    │              │    │(Repository) │
└─────────────┘    └──────────────┘    └─────────────┘
       │                   │                   │
       │ User Interaction  │                   │
       │ ─────────────────▶│                   │
       │                   │ Data Request      │
       │                   │ ─────────────────▶│
       │                   │                   │
       │                   │ Data Response     │
       │                   │ ◀─────────────────│
       │ State Update      │                   │
       │ ◀─────────────────│                   │
       │                   │                   │
```

## Benefits of This Architecture

### **Separation of Concerns**
- **Views**: Only handle UI rendering
- **ViewModels**: Handle UI logic and state management
- **Repositories**: Handle data operations
- **Models**: Define data structure

### **Testability**
```kotlin
// Easy to test ViewModels
class AuthViewModelTest {
    @Test
    fun `login with valid credentials should succeed`() {
        // Mock repository
        // Test ViewModel logic
        // Assert expected state changes
    }
}
```

### **Maintainability**
- Clear code organization
- Easy to add new features
- Changes in one layer don't affect others

### **Scalability**
- Easy to add new screens/features
- Consistent patterns across the app
- Easy to refactor or replace components

This architecture makes the Android client robust, testable, and maintainable while following Android development best practices.
