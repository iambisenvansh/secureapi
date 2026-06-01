import { useEffect, useState } from "react";
import { getSummary } from "../api/AdminApi";
import StatCard from "../components/cards/StatCard";
import Layout from "../components/Layout/Layout";
import ThreatPie from "../components/charts/ThreatPie";
import ThreatBar from "../components/charts/ThreatBar";

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;

    const fetchSummary = async () => {
      try {
        const res = await getSummary();

        if (isMounted && res?.data) {
          setData(res.data);
          setError("");
        }
      } catch (err) {
        console.error("Summary fetch failed:", err);

        if (isMounted) {
          setError(
            err?.response?.status === 403
              ? "Unauthorized: Admin access required"
              : "Failed to load analytics data"
          );
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    fetchSummary();
    return () => (isMounted = false);
  }, []);

  return (
    <Layout>
      {/* 🔄 Loading */}
      {loading && (
        <p className="text-gray-400 p-6">Loading analytics...</p>
      )}

      {/* ❌ Error */}
      {!loading && error && (
        <p className="text-red-400 p-6">{error}</p>
      )}

      {/* ✅ Success */}
      {!loading && !error && data && (
        <>
          <h1 className="text-2xl font-bold text-white mb-6">
            Security Overview
          </h1>

          {/* 🔢 STAT CARDS */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <StatCard title="Total Requests" value={data.total ?? 0} color="blue" />
            <StatCard title="Normal" value={data.normal ?? 0} color="green" />
            <StatCard title="Suspicious" value={data.suspicious ?? 0} color="yellow" />
            <StatCard title="Malicious" value={data.malicious ?? 0} color="red" />
          </div>

          {/* 📊 THREAT CHARTS */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-10">
            <ThreatPie data={data} />
            <ThreatBar data={data} />
          </div>
        </>
      )}
    </Layout>
  );
}
