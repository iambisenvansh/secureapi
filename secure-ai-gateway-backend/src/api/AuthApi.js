// src/api/AuthApi.js
import api from "./httpClient";

export async function login(username, password) {
  const res = await api.post("/auth/login", { username, password });
  return res.data; // { token: "..." }
}

export async function register(username, email, password) {
  const res = await api.post("/auth/register", { username, email, password });
  return res.data;
}
