import { Link, NavLink } from "react-router-dom";

export default function Sidebar() {
  const linkClass =
    "block px-3 py-2 rounded hover:bg-gray-800 hover:text-blue-400 transition";

  return (
    <aside className="w-64 bg-gray-900 text-gray-200 min-h-screen p-6">
      <h2 className="text-xl font-bold mb-8 text-blue-400">
        Secure AI Gateway
      </h2>

      <nav className="space-y-2">
        <NavLink to="/dashboard" className={linkClass}>
          Dashboard
        </NavLink>
        <NavLink to="/analytics" className={linkClass}>
          Analytics
        </NavLink>
        <NavLink to="/threats" className={linkClass}>
          Threats
        </NavLink>
        <NavLink to="/logs" className={linkClass}>
          Logs
        </NavLink>
      </nav>
    </aside>
  );
}
