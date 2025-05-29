# GourmetGo - School Project

## Description
GourmetGo is a mobile platform developed as a **school project for ITCR** to discover and book unique culinary experiences. It allows users to explore themed dinners, cooking classes, and special events, while chefs and restaurants can manage their experiences, participants, and content.

## Key Features
- 🔐 **Authentication** for users and chefs
- 📱 **QR code bookings**
- 🍽️ **Culinary experience management**
- ⭐ **Rating system**
- 🤖 **Support chatbot**
- 📧 **Email notifications**

---

## Project Structure

### Backend (Node.js + Express + MongoDB)
```
core/
├── app.js                     # Main entry point
├── package.json               # Backend dependencies
├── models/                    # MongoDB schemas
│   ├── userSchema.js         # User (user/chef)
│   ├── chefProfileSchema.js  # Chef profile
│   ├── experienceSchema.js   # Culinary experiences
│   ├── bookingSchema.js      # Reservations
│   ├── ratingSchema.js       # Ratings
│   └── supportMessageSchema.js
├── controllers/               # Business logic
│   ├── authController.js     # Authentication
│   ├── userController.js     # User management
│   ├── chefController.js     # Chef management
│   ├── experienceController.js
│   ├── bookingController.js
│   └── ratingController.js
├── routes/                    # API endpoints
│   ├── authRoutes.js
│   ├── userRoutes.js
│   ├── chefRoutes.js
│   ├── experienceRoutes.js
│   ├── bookingRoutes.js
│   └── ratingRoutes.js
├── middlewares/               # Custom middleware
│   ├── auth.js               # JWT authentication
│   ├── validateUser.js
│   ├── validateChef.js
│   └── errorHandler.js
├── utils/                     # Utilities
│   ├── validators.js         # Input validation
│   ├── mailer.js            # Email service
│   ├── pdfkit.js            # PDF generation
│   ├── bookingUtils.js      # QR code generation
│   └── deleteCode.js
└── extra/mail/               # Email templates
    ├── welcome-user.html
    ├── welcome-chef.html
    ├── booking-confirmation.html
    └── experience-created.html
```

### Frontend (Android - Kotlin + Jetpack Compose)
```
client/app/src/main/java/gourmetgo/
├── AppMain.kt                 # Main activity
├── Config.kt                  # App configuration
├── data/                      # Data layer
│   ├── models/               # Data models
│   │   ├── User.kt
│   │   ├── Experience.kt
│   │   └── dtos/
│   │       ├── LoginRequest.kt
│   │       ├── LoginResponse.kt
│   │       └── ExperiencesResponse.kt
│   ├── repository/           # Repository pattern
│   │   ├── AuthRepository.kt
│   │   └── ExperiencesRepository.kt
│   ├── remote/               # Network layer
│   │   ├── ApiService.kt
│   │   └── Connection.kt
│   ├── localStorage/         # Local storage
│   │   └── SharedPrefsManager.kt
│   └── mockups/              # Mock data
│       ├── UserMockup.kt
│       └── ExperiencesMockup.kt
├── viewmodel/                # MVVM ViewModels
│   ├── AuthViewModel.kt
│   ├── ExperiencesViewModel.kt
│   ├── statesUi/            # UI states
│   │   ├── AuthUiState.kt
│   │   └── ExperienceUiState.kt
│   └── factories/           # ViewModel factories
│       ├── AuthViewModelFactory.kt
│       └── ExperiencesViewModelFactory.kt
├── ui/                       # UI layer
│   ├── screens/             # Screen composables
│   │   ├── LoginScreen.kt
│   │   └── ExperienceScreen.kt
│   ├── components/          # Reusable components
│   │   ├── ExperienceCard.kt
│   │   ├── FilterChip.kt
│   │   ├── LoginHeader.kt
│   │   └── TestUserInfoCard.kt
│   ├── navigation/          # Navigation
│   │   └── MainNavigation.kt
│   └── theme/               # Material Design theme
│       ├── Theme.kt
│       └── Typography.kt
└── utils/
    └── Empty.kt
```

## Architecture Pattern: MVVM (Model-View-ViewModel)

### MVVM Implementation:

#### **Model Layer**
- **Data Models**: `User.kt`, `Experience.kt`
- **Repositories**: Handle data operations and business logic
- **Remote/Local Data Sources**: API calls and local storage

#### **View Layer**
- **Composable Screens**: UI components using Jetpack Compose
- **Navigation**: Handles screen transitions
- **UI Components**: Reusable composable components

#### **ViewModel Layer**
- **ViewModels**: `AuthViewModel`, `ExperiencesViewModel`
- **UI States**: Manage UI state with data classes
- **Business Logic**: Handle user interactions and data flow

### MVVM Benefits in This Project:
- ✅ **Separation of Concerns**: Clear separation between UI and business logic
- ✅ **Testability**: ViewModels can be easily unit tested
- ✅ **Lifecycle Awareness**: ViewModels survive configuration changes
- ✅ **Data Binding**: UI automatically updates when data changes
- ✅ **Maintainability**: Code is organized and easy to maintain

## Technology Stack

### Backend
- **Node.js** with Express.js
- **MongoDB** with Mongoose ODM
- **JWT** for authentication
- **Nodemailer** for email services
- **PDFKit** for PDF generation
- **QRCode** library for QR generation

### Frontend
- **Android** (Kotlin)
- **Jetpack Compose** for modern UI
- **Material Design 3**
- **Retrofit** for API calls
- **Coroutines** for asynchronous operations
- **ViewModel & LiveData** for MVVM
- **SharedPreferences** for local storage

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/register-chef` - Chef registration
- `POST /api/auth/login` - Login (users and chefs)

### Experiences
- `GET /api/experiences` - List experiences (with filters)
- `POST /api/experiences` - Create experience (chef only)
- `GET /api/experiences/:id` - Experience details
- `PUT /api/experiences/:id` - Update experience (chef only)
- `DELETE /api/experiences/:id` - Delete experience (with verification)

### Bookings
- `POST /api/bookings` - Create booking
- `GET /api/bookings/my` - User's bookings
- `PUT /api/bookings/:id/cancel` - Cancel booking

### Ratings
- `POST /api/ratings` - Create rating
- `GET /api/ratings/experience/:id` - Experience ratings

## Features

### User Features
- Register and login
- Browse culinary experiences
- Filter by category and search
- Make reservations with QR codes
- Rate and review experiences
- Manage bookings

### Chef Features
- Register as chef/restaurant
- Create and manage experiences
- View bookings for their experiences
- Update experience details
- Delete experiences (with email verification)

### System Features
- Email notifications (welcome, booking confirmation, etc.)
- PDF generation for booking confirmations
- QR code generation for entry verification
- Input validation and security
- Error handling and logging

## Development Notes

This is a **school project** demonstrating:
- Full-stack development skills
- MVVM architecture implementation
- RESTful API design
- Modern Android development with Compose
- Database design and management
- Email service integration
- File generation (PDF, QR codes)
- Authentication and authorization

The project includes both real API integration and mock data for testing purposes, making it suitable for development and demonstration.
