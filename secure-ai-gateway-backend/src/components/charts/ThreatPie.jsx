import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";
import { Pie } from "react-chartjs-2";

ChartJS.register(ArcElement, Tooltip, Legend);

export default function ThreatPie({ data }) {
  if (!data) return null;

  const chartData = {
    labels: ["Normal", "Suspicious", "Malicious"],
    datasets: [
      {
        data: [
          data.normal ?? 0,
          data.suspicious ?? 0,
          data.malicious ?? 0,
        ],
        backgroundColor: ["#22c55e", "#eab308", "#ef4444"],
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false, // 🔥 IMPORTANT
    plugins: {
      legend: {
        labels: {
          color: "#cbd5e1",
        },
      },
    },
  };

  return (
    <div className="bg-gray-800 p-6 rounded-xl">
      <h2 className="text-white font-semibold mb-4">
        Threat Distribution
      </h2>

      {/* 🔽 CONTROL SIZE HERE */}
      <div className="w-full h-[300px] flex justify-center">
        <Pie data={chartData} options={options} />
      </div>
    </div>
  );
}
