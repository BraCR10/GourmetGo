## Rutas de la API

### Rutas de Autenticación
- `POST   /api/auth/register`         // Registro de usuario
- `POST   /api/auth/register-chef`    // Registro de chef o restaurante
- `POST   /api/auth/login`            // Inicio de sesión (usuarios y chefs)
- `POST   /api/auth/logout`           // Cierre de sesión
- `POST   /api/auth/refresh`          // Refrescar token

### Rutas de Usuarios
- `GET    /api/users/me`              // Obtener perfil propio
- `PUT    /api/users/me`              // Actualizar perfil propio
- `DELETE /api/users/me`              // Eliminar cuenta
- `GET    /api/users/:id`             // Obtener perfil público de usuario

### Rutas de Chefs/Restaurantes
- `GET    /api/chefs`                 // Listar chefs/restaurantes
- `GET    /api/chefs/:id`             // Detalle de chef/restaurante
- `PUT    /api/chefs/:id`             // Actualizar perfil de chef (solo dueño)
- `GET    /api/chefs/:id/experiences` // Listar experiencias de un chef

### Rutas de Experiencias
- `GET    /api/experiences`           // Listar experiencias (con filtros)
- `POST   /api/experiences`           // Crear experiencia (chef)
- `GET    /api/experiences/:id`       // Detalle de experiencia
- `PUT    /api/experiences/:id`       // Editar experiencia (chef)
- `DELETE /api/experiences/:id`       // Eliminar experiencia (chef)

### Rutas de Reservas
- `GET    /api/bookings`              // Listar reservas propias
- `POST   /api/bookings`              // Crear reserva
- `GET    /api/bookings/:id`          // Detalle de reserva
- `PUT    /api/bookings/:id/cancel`   // Cancelar reserva
- `GET    /api/chefs/:id/bookings`    // Reservas de mis experiencias (chef)

### Rutas de Valoraciones
- `POST   /api/ratings`               // Crear valoración
- `GET    /api/experiences/:id/ratings` // Valoraciones de una experiencia

### Rutas de Pagos
- `POST   /api/payments`              // Procesar pago de reserva
- `GET    /api/payments/:id/status`   // Estado de pago

### Rutas de Soporte/Chatbot
- `POST   /api/support/message`       // Enviar mensaje al soporte/chatbot
- `GET    /api/support/history`       // Historial de soporte

### Rutas de Utilidades
- `GET    /api/experiences/:id/qrcode` // Obtener QR para reserva/entrada

---

## Validaciones y Middlewares

- Las rutas `/api/auth/register` y `/api/auth/register-chef` usan middlewares de validación (`validateUser`, `validateChef`) para asegurar que los datos cumplen con los requisitos del proyecto.
- Las validaciones incluyen: formato de correo, teléfono (8 dígitos), identificación (9 dígitos para usuarios), contraseña (mínimo 6 letras, 4 números y un punto), y que la foto sea una URL de Cloudinary.
- El login es común para usuarios y chefs.

---

## Modelos de Datos (MongoDB)

### Usuario (`User`)
- **name**: Nombre completo del usuario (`String`)
- **email**: Correo electrónico único (`String`)
- **phone**: Número telefónico (`String`)
- **identification**: Identificación (solo usuarios) (`String`)
- **password**: Contraseña cifrada (`String`)
- **role**: Rol del usuario (`String`, valores: `user`, `chef`)
- **avatar**: URL de la imagen de perfil (`String`)
- **preferences**: Preferencias culinarias (`[String]`)
- **createdAt**: Fecha de creación (`Date`)

### Perfil de Chef (`ChefProfile`)
- **user**: Referencia al usuario (`ObjectId` → `User`)
- **contactPerson**: Persona de contacto (`String`)
- **location**: Ubicación del establecimiento (`String`)
- **cuisineType**: Tipo de cocina que ofrece (`String`)
- **bio**: Biografía del chef (`String`)
- **experience**: Experiencia o especialidad (`String`)
- **socialLinks**: Enlaces a redes sociales (`[String]`)

### Experiencia (`Experience`)
- **title**: Título de la experiencia (`String`)
- **description**: Descripción detallada (`String`)
- **chef**: Referencia al chef que la ofrece (`ObjectId` → `ChefProfile`)
- **category**: Categoría o tipo de experiencia (`String`)
- **location**: Ubicación (`String`)
- **date**: Fecha y hora (`Date`)
- **price**: Precio por persona (`Number`)
- **capacity**: Cupo máximo de participantes (`Number`)
- **images**: Imágenes asociadas (`[String]`)
- **ratings**: Referencias a valoraciones (`[ObjectId]` → `Rating`)
- **createdAt**: Fecha de creación (`Date`)

### Reserva (`Booking`)
- **user**: Referencia al usuario (`ObjectId` → `User`)
- **experience**: Referencia a la experiencia (`ObjectId` → `Experience`)
- **status**: Estado de la reserva (`String`, valores: `pending`, `confirmed`, `cancelled`)
- **qrCode**: Código QR para validación de asistencia (`String`)
- **payment**: Referencia al pago realizado (`ObjectId` → `Payment`)
- **createdAt**: Fecha de creación (`Date`)

### Valoración (`Rating`)
- **user**: Referencia al usuario (`ObjectId` → `User`)
- **experience**: Referencia a la experiencia (`ObjectId` → `Experience`)
- **score**: Puntuación (1 a 5) (`Number`)
- **comment**: Comentario del usuario (`String`)
- **createdAt**: Fecha de creación (`Date`)

### Pago (`Payment`)
- **user**: Referencia al usuario (`ObjectId` → `User`)
- **booking**: Referencia a la reserva (`ObjectId` → `Booking`)
- **amount**: Monto pagado (`Number`)
- **status**: Estado del pago (`String`, valores: `pending`, `completed`, `failed`)
- **method**: Método de pago (`String`)
- **createdAt**: Fecha de creación (`Date`)

### Mensaje de Soporte (`SupportMessage`)
- **user**: Referencia al usuario (`ObjectId` → `User`)
- **message**: Mensaje enviado (`String`)
- **response**: Respuesta del soporte/chatbot (`String`)
- **createdAt**: Fecha de creación (`Date`)

---

**Notas:**
- Las imágenes deben ser subidas a Cloudinary desde el frontend y solo se almacena la URL en la base de datos.
- Las validaciones de datos se realizan mediante middlewares antes de llegar a los controladores.
- El registro de usuario y chef/restaurante son rutas separadas y cada una valida los campos requeridos según el tipo.