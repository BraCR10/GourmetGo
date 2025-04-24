const mongoose = require('mongoose');

const RatingSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  experience: { type: mongoose.Schema.Types.ObjectId, ref: 'Experience', required: true },
  score: { type: Number, min: 1, max: 5, required: true },
  comment: String,
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Rating', RatingSchema);