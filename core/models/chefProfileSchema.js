const mongoose = require('mongoose');

const ChefProfileSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  bio: String,
  experience: String,
  socialLinks: [String]
});

module.exports = mongoose.model('ChefProfile', ChefProfileSchema);