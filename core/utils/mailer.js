const nodemailer = require('nodemailer');
const fs = require('fs');
const path = require('path');
const handlebars = require('handlebars');

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
 * Enviar correo electrónico usando plantilla HTML y adjuntos opcionales.
 * @param {string} to - Destinatario
 * @param {string} subject - Asunto
 * @param {string} templateName - Nombre del archivo de plantilla (ej: 'delete-experience-code.html')
 * @param {object} variables - Variables para la plantilla
 * @param {Array} attachments - (Opcional) Array de adjuntos [{filename, path}]
 */
exports.sendMailTemplate = async (to, subject, templateName, variables, attachments = []) => {
  const templatePath = path.join(__dirname, '../extra/mail', templateName);
  const html = renderTemplate(templatePath, variables);
  const mailOptions = {
    from: process.env.SMTP_USER, 
    to,
    subject,
    html,
    attachments
  };
  await transporter.sendMail(mailOptions);
};