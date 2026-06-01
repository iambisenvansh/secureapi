// src/pages/Login.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/AuthApi";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();
  const { setToken } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const data = await login(username, password);

      if (data && data.token) {
        localStorage.setItem("token", data.token);
        setToken(data.token);
        navigate("/dashboard");
      } else {
        setError("Invalid login response from server");
      }
    } catch {
      setError("Invalid username or password");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-900 via-gray-800 to-black text-white">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-sm bg-gray-900/80 backdrop-blur-xl border border-gray-700 rounded-xl p-8 shadow-2xl"
      >
        {/* Title */}
        <h2 className="text-3xl font-bold text-center mb-2">
          Secure AI Gateway
        </h2>
        <p className="text-gray-400 text-center mb-6 text-sm">
          Admin Access Only
        </p>

        {/* Error */}
        {error && (
          <div className="mb-4 text-sm text-red-400 text-center bg-red-900/20 border border-red-500/30 rounded p-2">
            {error}
          </div>
        )}

        {/* Username */}
        <div className="mb-4">
          <label className="block text-sm text-gray-400 mb-1">
            Username
          </label>
          <input
            type="text"
            placeholder="Enter admin username"
            className="w-full px-3 py-2 rounded-md bg-gray-800 border border-gray-700 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none transition"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        {/* Password */}
        <div className="mb-6">
          <label className="block text-sm text-gray-400 mb-1">
            Password
          </label>
          <input
            type="password"
            placeholder="Enter password"
            className="w-full px-3 py-2 rounded-md bg-gray-800 border border-gray-700 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 outline-none transition"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        {/* Button */}
        <button
          type="submit"
          className="w-full py-2 rounded-md bg-blue-600 hover:bg-blue-700 transition font-semibold tracking-wide"
        >
          Login
        </button>

        {/* Footer */}
        <p className="mt-6 text-xs text-gray-500 text-center">
          © Secure AI Gateway
        </p>
      </form>
    </div>
  );
}
