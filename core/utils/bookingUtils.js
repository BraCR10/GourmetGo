const { v4: uuidv4 } = require('uuid');
const QRCode = require('qrcode');

/**
 * Genera un código único de reservación.
 */
exports.generateBookingCode = () => uuidv4().split('-')[0].toUpperCase();

/**
 * Genera un array de códigos QR (uno por cada persona).
 * @param {string} bookingCode
 * @param {number} people
 * @returns {Promise<string[]>} Array de dataURL QR
 */
exports.generateQRCodes = async (bookingCode, people) => {
  const qrCodes = [];
  for (let i = 0; i < people; i++) {
    const qrData = `${bookingCode}-${i + 1}`;
    const qr = await QRCode.toDataURL(qrData);
    qrCodes.push(qr);
  }
  return qrCodes;
};