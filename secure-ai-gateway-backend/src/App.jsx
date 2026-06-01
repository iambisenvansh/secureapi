import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Navbar from "./components/Layout/Navbar";
import { useAuth } from "./context/AuthContext";

export default function App() {
  const { isAuthenticated } = useAuth();

  return (
    <>
      {/* {isAuthenticated && <Navbar />} */}

      <Routes>
        {/* 👇 FIX: root route */}
        <Route
          path="/"
          element={
            isAuthenticated ? (
              <Navigate to="/dashboard" />
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />}
        />

        <Route
          path="/dashboard"
          element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
        />

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </>
  );
}
