const User = require('../models/userSchema');
const { validateUserUpdate } = require('../utils/validators');

exports.updateMe = async (req, res) => {
  try {
    const userId = req.user.userId; 
    const { name, password, ...fieldsToUpdate } = req.body;

    if (name !== undefined || password !== undefined) {
      return res.status(400).json({ message: 'No se puede modificar el nombre ni la contraseña.' });
    }

    const error = validateUserUpdate(fieldsToUpdate);
    if (error) return res.status(400).json({ message: error });

    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $set: fieldsToUpdate },
      { new: true }
    ).select('-password');

    res.json({ message: 'Perfil actualizado correctamente.', user: updatedUser });
  } catch (err) {
    res.status(500).json({ message: 'Error al actualizar el perfil.', error: err.message });
  }
};