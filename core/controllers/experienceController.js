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