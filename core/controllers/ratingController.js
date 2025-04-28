const Rating = require('../models/ratingSchema');

exports.createRating = async (req, res) => {
  try {
    const userId = req.user.userId;
    const { experienceId, score, comment } = req.body;

    if (!experienceId || !score) {
      return res.status(400).json({ message: 'experienceId y score son obligatorios.' });
    }

    // (Opcional) Validar que el usuario haya reservado y asistido a la experiencia

    const rating = new Rating({
      user: userId,
      experience: experienceId,
      score,
      comment
    });
    await rating.save();
    res.status(201).json({ message: 'Valoración creada exitosamente.', rating });
  } catch (err) {
    res.status(500).json({ message: 'Error al crear la valoración.', error: err.message });
  }
};

exports.getExperienceRatings = async (req, res) => {
  try {
    const ratings = await Rating.find({ experience: req.params.id })
      .populate('user', 'name avatar');
    res.json(ratings);
  } catch (err) {
    res.status(500).json({ message: 'Error al obtener las valoraciones.', error: err.message });
  }
};