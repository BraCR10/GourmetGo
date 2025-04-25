const mongoose = require('mongoose');

const BookingSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  experience: { type: mongoose.Schema.Types.ObjectId, ref: 'Experience', required: true },
  name: { type: String, required: true },
  email: { type: String, required: true }, 
  phone: { type: String, required: true }, 
  people: { type: Number, required: true }, 
  termsAccepted: { type: Boolean, required: true },
  paymentMethod: { type: String, enum: ['Pago en el lugar', 'Transferencia'], required: true },
  status: { type: String, enum: ['pending', 'confirmed', 'cancelled', 'expired'], default: 'pending' },
  bookingCode: { type: String, required: true, unique: true },
  qrCodes: [{ type: String }], // Array de dataURL QR
  payment: { type: mongoose.Schema.Types.ObjectId, ref: 'Payment' },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Booking', BookingSchema);