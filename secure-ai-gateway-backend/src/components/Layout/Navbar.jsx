import { useAuth } from "../../context/AuthContext";

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth();

  return (
    <nav className="flex justify-between items-center px-6 py-4 bg-gray-900 text-white shadow">
      <h1 className="font-bold text-lg">Secure AI Gateway</h1>

      {isAuthenticated && (
        <button
          onClick={logout}
          className="bg-red-600 px-4 py-2 rounded hover:bg-red-700 transition"
        >
          Logout
        </button>
      )}
    </nav>
  );
}
