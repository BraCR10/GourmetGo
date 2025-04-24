const mongoose = require('mongoose');

const ExperienceSchema = new mongoose.Schema({
  title: { type: String, required: true },
  description: String,
  chef: { type: mongoose.Schema.Types.ObjectId, ref: 'ChefProfile', required: true },
  category: String,
  location: String,
  date: Date,
  price: Number,
  capacity: Number,
  images: [String],
  ratings: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Rating' }],
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Experience', ExperienceSchema);