const { validateExperienceEdit } = require('../utils/validators');

module.exports = (req, res, next) => {
  const error = validateExperienceEdit(req.body);
  if (error) return res.status(400).json({ message: error });
  next();
};