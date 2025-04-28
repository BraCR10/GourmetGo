const express = require('express');
const router = express.Router();
const experienceController = require('../controllers/experienceController');
const auth = require('../middlewares/auth');
const validateExperience = require('../middlewares/validateExperience');
const validateExperienceEdit = require('../middlewares/validateExperienceEdit');

// Create experience (solo chefs autenticados)
router.post('/', auth, validateExperience, experienceController.createExperience);

// Edit experience (solo chefs autenticados)
router.put('/:id', auth, validateExperienceEdit, experienceController.updateExperience);

// Solicitar código de eliminación
router.post('/:id/request-delete', auth, experienceController.requestDeleteExperience);

// Eliminar experiencia (requiere código)
router.delete('/:id', auth, experienceController.deleteExperience);

// Listar experiencias (con filtros opcionales: ?q=, ?chef=, ?date=, etc)
router.get('/', experienceController.listExperiences);

// Detalle de experiencia por ID
router.get('/:id', experienceController.getExperienceDetail);

module.exports = router;