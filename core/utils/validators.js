const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^\d{8}$/;
const idRegex = /^\d{9}$/;
const passwordRegex = /^(?=(?:[^a-zA-Z]*[a-zA-Z]){6,})(?=(?:[^0-9]*[0-9]){4,})(?=.*\.)[a-zA-Z0-9.]{11,}$/;

exports.validateUserRegistration = ({ name, email, phone, identification, password, photoUrl }) => {
  if (!name || !email || !phone || !identification || !password || !photoUrl) {
    return 'Todos los campos obligatorios deben ser completados.';
  }
  if (!emailRegex.test(email)) return 'Formato de correo electrónico inválido.';
  if (!phoneRegex.test(phone)) return 'El número telefónico debe tener 8 dígitos.';
  if (!idRegex.test(identification)) return 'La identificación debe tener 9 dígitos.';
  if (!passwordRegex.test(password)) return 'La contraseña debe tener al menos 6 letras, 4 números y un punto.';
  if (!photoUrl.startsWith('https://res.cloudinary.com/')) return 'La fotografía debe ser una URL válida de Cloudinary.';
  return null;
};

exports.validateChefRegistration = ({ name, contactPerson, email, phone, location, cuisineType, password, photoUrl }) => {
  if (!name || !contactPerson || !email || !phone || !location || !cuisineType || !password || !photoUrl) {
    return 'Todos los campos obligatorios deben ser completados.';
  }
  if (!emailRegex.test(email)) return 'Formato de correo electrónico inválido.';
  if (!phoneRegex.test(phone)) return 'El número telefónico debe tener 8 dígitos.';
  if (!passwordRegex.test(password)) return 'La contraseña debe tener al menos 6 letras, 4 números y un punto.';
  if (!photoUrl.startsWith('https://res.cloudinary.com/')) return 'La fotografía debe ser una URL válida de Cloudinary.';
  return null;
};

exports.validateUserUpdate = (fields) => {
  const { email, phone, identification, photoUrl, preferences } = fields;
  if (email && !emailRegex.test(email)) return 'Formato de correo electrónico inválido.';
  if (phone && !phoneRegex.test(phone)) return 'El número telefónico debe tener 8 dígitos.';
  if (identification && !idRegex.test(identification)) return 'La identificación debe tener 9 dígitos.';
  if (photoUrl && !photoUrl.startsWith('https://res.cloudinary.com/')) return 'La fotografía debe ser una URL válida de Cloudinary.';
  if (preferences && !Array.isArray(preferences)) return 'Las preferencias deben ser un arreglo.';
  return null;
};

exports.validateChefUpdate = (fields) => {
  const { contactPerson, phone, location, cuisineType, photoUrl, bio, experience, socialLinks } = fields;
  if (phone && !/^\d{8}$/.test(phone)) return 'El número telefónico debe tener 8 dígitos.';
  if (photoUrl && !photoUrl.startsWith('https://res.cloudinary.com/')) return 'La fotografía debe ser una URL válida de Cloudinary.';
  if (socialLinks && !Array.isArray(socialLinks)) return 'Las redes sociales deben ser un arreglo.';
  return null;
};