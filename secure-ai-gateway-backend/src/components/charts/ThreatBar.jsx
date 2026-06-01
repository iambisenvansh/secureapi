import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";

// ✅ REQUIRED registration
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend
);

export default function ThreatBar({ data }) {
  if (!data) return null;

  const chartData = {
    labels: ["Normal", "Suspicious", "Malicious"],
    datasets: [
      {
        label: "Requests",
        data: [
          data.normal ?? 0,
          data.suspicious ?? 0,
          data.malicious ?? 0,
        ],
        backgroundColor: "#3b82f6",
      },
    ],
  };

  return (
    <div className="bg-gray-800 p-6 rounded-xl">
      <h2 className="text-white font-semibold mb-4">
        Threat Count Overview
      </h2>
      <Bar data={chartData} />
    </div>
  );
}
