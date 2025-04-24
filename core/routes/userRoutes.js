const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const validateUserUpdate = require('../middlewares/validateUserUpdate');
const auth = require('../middlewares/auth');


router.put('/me', auth, validateUserUpdate, userController.updateMe);

module.exports = router;