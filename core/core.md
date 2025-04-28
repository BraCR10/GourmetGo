# GourmetGo - Documentación Core API

---

## Rutas de la API (Checklist)

### Autenticación
- [x] `POST   /api/auth/register`         — Registro de usuario
- [x] `POST   /api/auth/register-chef`    — Registro de chef/restaurante
- [x] `POST   /api/auth/login`            — Inicio de sesión (usuarios y chefs)


### Usuarios
- [x] `PUT    /api/users/me`              — Actualizar perfil propio
- [X] `GET    /api/users/me`              — Obtener perfil propio
- [X] `GET    /api/users/:id`             — Obtener perfil público de usuario

### Chefs/Restaurantes
- [x] `GET    /api/chefs`                 — Listar chefs/restaurantes
- [x] `GET    /api/chefs/:id`             — Detalle de chef/restaurante
- [x] `PUT    /api/chefs/me`              — Actualizar perfil de chef (solo dueño)
- [X] `GET    /api/chefs/:id/experiences` — Listar experiencias de un chef

### Experiencias
- [X] `GET    /api/experiences`           — Listar experiencias (con filtros)
- [x] `POST   /api/experiences`           — Crear experiencia (chef)
- [X] `GET    /api/experiences/:id`       — Detalle de experiencia
- [x] `PUT    /api/experiences/:id`       — Editar experiencia (chef, solo campos permitidos)
- [x] `POST   /api/experiences/:id/request-delete` — Solicitar código para eliminar experiencia
- [x] `DELETE /api/experiences/:id`       — Eliminar experiencia (chef, requiere código)

### Reservas
- [X] `GET    /api/bookings`              — Listar reservas propias
- [X] `POST   /api/bookings`              — Crear reserva
- [X] `GET    /api/bookings/:id`          — Detalle de reserva
- [X] `PUT    /api/bookings/:id/cancel`   — Cancelar reserva
- [X] `GET    /api/chefs/:id/bookings`    — Reservas de mis experiencias (chef)

### Valoraciones
- [X] `POST   /api/ratings`               — Crear valoración
- [X] `GET    /api/experiences/:id/ratings` — Valoraciones de una experiencia

### Soporte/Chatbot
- [ ] `POST   /api/support/message`       — Enviar mensaje al soporte/chatbot
- [ ] `GET    /api/support/history`       — Historial de soporte

### Utilidades
- [ ] `GET    /api/experiences/:id/qrcode` — Obtener QR para reserva/entrada

---

## Validaciones y Middlewares

- Todas las rutas críticas usan middlewares de validación y autenticación (`auth`).
- Validaciones incluyen: formato de correo, teléfono (8 dígitos), identificación (9 dígitos para usuarios), contraseña (mínimo 6 letras, 4 números y un punto), foto (URL Cloudinary), fechas futuras, precios y capacidad positivos, enlaces válidos, etc.
- El login es común para usuarios y chefs.
- El registro de usuario y chef/restaurante son rutas separadas y cada una valida los campos requeridos según el tipo.

---

## Modelos de Datos (MongoDB)

### Usuario (`User`)
- `name`: Nombre completo (`String`)
- `email`: Correo electrónico único (`String`)
- `phone`: Número telefónico (`String`)
- `identification`: Identificación (solo usuarios) (`String`)
- `password`: Contraseña cifrada (`String`)
- `role`: Rol (`String`: `user` o `chef`)
- `avatar`: URL de la imagen de perfil (`String`)
- `preferences`: Preferencias culinarias (`[String]`)
- `createdAt`: Fecha de creación (`Date`)

### Perfil de Chef (`ChefProfile`)
- `user`: Referencia al usuario (`ObjectId` → `User`)
- `contactPerson`: Persona de contacto (`String`)
- `phone`: Teléfono de contacto (`String`)
- `location`: Ubicación (`String`)
- `cuisineType`: Tipo de cocina (`String`)
- `photoUrl`: Foto del chef/restaurante (`String`)
- `bio`: Biografía (`String`)
- `experience`: Experiencia o especialidad (`String`)
- `socialLinks`: Redes sociales (`[String]`)

### Experiencia (`Experience`)
- `title`: Título (`String`)
- `description`: Descripción detallada (`String`)
- `chef`: Referencia al chef (`ObjectId` → `ChefProfile`)
- `category`: Categoría o tipo (`String`)
- `location`: Enlace de ubicación (`String`)
- `date`: Fecha y hora (`Date`)
- `price`: Precio por persona (`Number`)
- `capacity`: Cupo máximo (`Number`)
- `duration`: Duración en horas (`Number`)
- `images`: Imágenes (`[String]`)
- `requirements`: Requisitos/recomendaciones (`String`, opcional)
- `status`: Estado (`String`: `Activa`, `Agotada`, `Próximamente`)
- `menu`: `{ image: String, text: String }` (al menos uno requerido)
- `createdAt`: Fecha de creación (`Date`)

### Reserva (`Booking`)
- `user`: Referencia al usuario (`ObjectId` → `User`)
- `experience`: Referencia a la experiencia (`ObjectId` → `Experience`)
- `status`: Estado de la reservación (`String`):
  - `pending`: Reservación creada, esperando a que ocurra el evento.
  - `confirmed`: Asistencia confirmada por el chef/organizador.
  - `cancelled`: Reservación cancelada por el usuario o el sistema.
  - `expired` (opcional): El evento ya pasó y la reservación no fue confirmada.
- `qrCodes`: Array de códigos QR (`[String]`)
- `bookingCode`: Código único de reservación (`String`)
- `termsAccepted`: Confirmación de términos y condiciones (`Boolean`)
- `paymentMethod`: Método de pago elegido (`String`: `Pago en el lugar` o `Transferencia`)
- `createdAt`: Fecha de creación (`Date`)

### Valoración (`Rating`)
- `user`: Referencia al usuario (`ObjectId` → `User`)
- `experience`: Referencia a la experiencia (`ObjectId` → `Experience`)
- `score`: Puntuación (1 a 5) (`Number`)
- `comment`: Comentario (`String`)
- `createdAt`: Fecha de creación (`Date`)

### Pago (`Payment`)
- `user`: Referencia al usuario (`ObjectId` → `User`)
- `booking`: Referencia a la reserva (`ObjectId` → `Booking`)
- `amount`: Monto pagado (`Number`)
- `status`: Estado (`String`: `pending`, `completed`, `failed`)
- `method`: Método de pago (`String`)
- `createdAt`: Fecha de creación (`Date`)

### Mensaje de Soporte (`SupportMessage`)
- `user`: Referencia al usuario (`ObjectId` → `User`)
- `message`: Mensaje enviado (`String`)
- `response`: Respuesta del soporte/chatbot (`String`)
- `createdAt`: Fecha de creación (`Date`)

---

## Sistema de Correos y Notificaciones

- Se utiliza SMTP seguro para el envío de correos (configurable por `.env`).
- Plantillas HTML en `/core/extra/mail/` para:
  - Bienvenida de usuario y chef
  - Publicación y actualización de experiencia
  - Solicitud y confirmación de eliminación de experiencia
  - Cancelación de experiencia (para usuarios con reservas)
- Los correos se envían automáticamente en los eventos clave (registro, creación/edición/eliminación de experiencia, etc).

---

## Seguridad y Flujo de Eliminación de Experiencias

- **No se permite eliminar experiencias agotadas.**
- Para eliminar una experiencia:
  1. El chef solicita la eliminación y recibe un código de verificación por correo.
  2. Debe ingresar el código recibido para confirmar la eliminación.
  3. El sistema muestra advertencia de eliminación permanente.
  4. Si confirma, se elimina la experiencia y se notifica a los usuarios con reservas.
- Los códigos de eliminación se almacenan temporalmente en memoria (mejorable a Redis/BD).

---

## Mejoras Futuras

- Agregar campo virtual o respuesta enriquecida para mostrar el número de reservas en cada experiencia.
- Mejorar la gestión de códigos de eliminación (persistencia).
- Endpoint para listar experiencias con conteo de reservas.
- Mejoras en seguridad, logging y monitoreo.

---

**Estado:**  
El sistema cumple con los requerimientos principales del proyecto, con validaciones robustas, notificaciones automáticas y estructura modular lista para escalar.

---