const mongoose = require('mongoose');

const BookingSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  experience: { type: mongoose.Schema.Types.ObjectId, ref: 'Experience', required: true },
  status: { type: String, enum: ['pending', 'confirmed', 'cancelled'], default: 'pending' },
  qrCode: String,
  payment: { type: mongoose.Schema.Types.ObjectId, ref: 'Payment' },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Booking', BookingSchema);