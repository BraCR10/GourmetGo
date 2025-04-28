const express = require('express');
const router = express.Router();
const chefController = require('../controllers/chefController');
const auth = require('../middlewares/auth');
const validateChefUpdate = require('../middlewares/validateChefUpdate');

router.put('/me', auth, validateChefUpdate, chefController.updateMe);

router.get('/:id/experiences', chefController.getChefExperiences);

module.exports = router;