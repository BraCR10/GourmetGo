const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const app = express();


// Middlewares
app.use(cors());
app.use(express.json());

// Ping endpoint
app.get('/ping', (req, res) => {
  res.status(200).json({ message: 'pong' });
});


// Conecction to MongoDB
mongoose.connect(process.env.MONGODB_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
.then(() => {
  console.log('Connected to MongoDB');
  const PORT = process.env.PORT || 3000;
  app.listen(PORT, () => {
    console.log(`Server listing at ${PORT}`);
  });
})
.catch(err => {
  console.error('Error conneting to MongoDB:', err);
});

