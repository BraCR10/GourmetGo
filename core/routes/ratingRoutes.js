const express = require('express');
const router = express.Router();
const ratingController = require('../controllers/ratingController');
const auth = require('../middlewares/auth');

// Crear valoración
router.post('/', auth, ratingController.createRating);

// Obtener valoraciones de una experiencia
router.get('/experience/:id', ratingController.getExperienceRatings);

module.exports = router;