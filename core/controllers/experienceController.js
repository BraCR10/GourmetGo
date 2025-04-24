const Experience = require('../models/experienceSchema'); 

exports.createExperience = async (req, res) => {
  try {
    const chefId = req.user.userId; 
    const experienceData = { ...req.body, chef: chefId };
    const experience = new Experience(experienceData);
    await experience.save();
    res.status(201).json({ message: 'Experiencia creada exitosamente.', experience });
  } catch (err) {
    res.status(500).json({ message: 'Error al crear la experiencia.', error: err.message });
  }
};

exports.updateExperience = async (req, res) => {
  try {
    const experienceId = req.params.id;
    const { location, date, status, capacity, price } = req.body;

    const experience = await Experience.findById(experienceId);
    if (!experience) return res.status(404).json({ message: 'Experiencia no encontrada.' });

    if (status && experience.status === 'Próximamente' && status === 'Activa') {
      experience.status = status;
    } else if (status && status !== experience.status) {
      return res.status(400).json({ message: 'Solo se permite cambiar el estado de Próximamente a Activa.' });
    }

    if (location !== undefined) experience.location = location;
    if (date !== undefined) experience.date = date;
    if (capacity !== undefined) experience.capacity = capacity;
    if (price !== undefined) experience.price = price;

    await experience.save();

    res.json({ message: 'Experiencia actualizada correctamente.', experience });
  } catch (err) {
    res.status(500).json({ message: 'Error al actualizar la experiencia.', error: err.message });
  }
};