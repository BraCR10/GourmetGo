const Booking = require('../models/bookingSchema');
const Experience = require('../models/experienceSchema');
const mailer = require('../utils/mailer');
const { generateBookingCode, generateQRCodes } = require('../utils/bookingUtils');

exports.createBooking = async (req, res) => {
  try {
    const userId = req.user.userId; // <- del token JWT
    const { experienceId, people, name, email, phone, termsAccepted, paymentMethod } = req.body;

    // Validaciones básicas
    if (!experienceId || !people || !name || !email || !phone || !termsAccepted || !paymentMethod) {
      return res.status(400).json({ message: 'Todos los campos son obligatorios.' });
    }
    if (!['Pago en el lugar', 'Transferencia'].includes(paymentMethod)) {
      return res.status(400).json({ message: 'Método de pago inválido.' });
    }
    if (!termsAccepted) {
      return res.status(400).json({ message: 'Debe aceptar los términos y condiciones.' });
    }

    // Buscar experiencia
    const experience = await Experience.findById(experienceId);
    if (!experience) return res.status(404).json({ message: 'Experiencia no encontrada.' });

    // Contar reservas existentes
    const totalBooked = await Booking.aggregate([
      { $match: { experience: experience._id } },
      { $group: { _id: null, total: { $sum: "$people" } } }
    ]);
    const bookedCount = totalBooked[0]?.total || 0;

    // Validar capacidad
    if (bookedCount + people > experience.capacity) {
      return res.status(400).json({ message: 'No hay suficientes cupos disponibles.' });
    }

    // Generar código único y QRs
    const bookingCode = generateBookingCode();
    const qrCodes = await generateQRCodes(bookingCode, people);

    // Crear reserva
    const booking = new Booking({
      user: userId, // <- asocia la reserva al usuario autenticado
      experience: experience._id,
      people,
      name,
      email,
      phone,
      termsAccepted,
      paymentMethod,
      status: 'pending',
      bookingCode,
      qrCodes
    });
    await booking.save();

    // (Opcional) Actualizar capacidad disponible (solo si quieres reflejarlo en el modelo)
    // experience.capacity = experience.capacity - people;
    // await experience.save();

    // Enviar correo de confirmación
    await mailer.sendMailTemplate(
      email,
      'Confirmación de reservación',
      'booking-confirmation.html',
      {
        name,
        experienceTitle: experience.title,
        date: experience.date.toLocaleString(),
        people,
        paymentMethod,
        bookingCode,
        qrCodesHtml: qrCodes.map(qr => `<img src="${qr}" width="120" />`).join(' '),
        year: new Date().getFullYear()
      }
    );

    res.status(201).json({ message: 'Reservación realizada exitosamente.', booking });
  } catch (err) {
    res.status(500).json({ message: 'Error al crear la reservación.', error: err.message });
  }
};

exports.getMyBookings = async (req, res) => {
  try {
    const userId = req.user.userId;
    const bookings = await Booking.find({ user: userId }).populate('experience');
    res.json(bookings);
  } catch (err) {
    res.status(500).json({ message: 'Error al obtener las reservaciones.', error: err.message });
  }
};