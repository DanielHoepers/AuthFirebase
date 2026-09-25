const { onCall } = require("firebase-functions/v2/https");

exports.saudacao = onCall({ region: "us-central1" }, () => ({
  message: "Olá do Firebase Cloud Functions!",
}));
