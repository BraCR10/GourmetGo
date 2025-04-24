const express = require('express');
const router = express.Router();
const experienceController = require('../controllers/experienceController');
const auth = require('../middlewares/auth');
const validateExperience = require('../middlewares/validateExperience');

router.post('/', auth, validateExperience, experienceController.createExperience);

module.exports = router;