const express = require('express');
const router = express.Router();
const bookingController = require('../controllers/bookingController');
const auth = require('../middlewares/auth');

// Crear reservación (requiere login)
router.post('/', auth, bookingController.createBooking);

// Listar reservaciones del usuario autenticado
router.get('/my', auth, bookingController.getMyBookings);

module.exports = router;