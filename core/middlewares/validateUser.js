const { validateUserRegistration } = require('../utils/validators');

module.exports = (req, res, next) => {
  const error = validateUserRegistration(req.body);
  if (error) return res.status(400).json({ message: error });
  next();
};