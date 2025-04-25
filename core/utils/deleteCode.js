exports.generateDeleteCode = () => {
  const numbers = Math.floor(1000 + Math.random() * 9000); // 4 números
  const letters = Math.random().toString(36).substring(2, 5).toUpperCase(); // 3 letras
  return `${numbers}${letters}`;
};