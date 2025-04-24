const nodemailer = require('nodemailer');
const fs = require('fs');
const path = require('path');

/**
 * Renderiza una plantilla HTML reemplazando {{variables}} por sus valores.
 * @param {string} templatePath - Ruta al archivo HTML.
 * @param {object} variables - Objeto con las variables a reemplazar.
 * @returns {string} HTML renderizado.
 */
function renderTemplate(templatePath, variables) {
  let html = fs.readFileSync(templatePath, 'utf8');
  for (const key in variables) {
    const regex = new RegExp(`{{\\s*${key}\\s*}}`, 'g');
    html = html.replace(regex, variables[key]);
  }
  return html;
}


const transporter = nodemailer.createTransport({
  host: 'mail.spacemail.com',
  port: 465,
  secure: true, 
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS
  }
});

/**
 * Enviar correo electrónico usando plantilla HTML.
 * @param {string} to - Destinatario
 * @param {string} subject - Asunto
 * @param {string} templateName - Nombre del archivo de plantilla (ej: 'delete-experience-code.html')
 * @param {object} variables - Variables para la plantilla
 */
exports.sendMailTemplate = async (to, subject, templateName, variables) => {
  const templatePath = path.join(__dirname, '../extra/mail', templateName);
  const html = renderTemplate(templatePath, variables);
  const mailOptions = {
    from: process.env.SMTP_USER, 
    to,
    subject,
    html
  };
  await transporter.sendMail(mailOptions);
};