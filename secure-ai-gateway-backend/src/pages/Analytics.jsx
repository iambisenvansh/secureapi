import { PieChart, Pie, Cell, Tooltip } from "recharts";
import Layout from "../components/Layout/Layout";

const COLORS = ["#22c55e", "#facc15", "#ef4444"];

export default function Analytics() {
  const data = [
    { name: "Normal", value: 25 },
    { name: "Suspicious", value: 27 },
    { name: "Malicious", value: 14 },
  ];

  return (
    <Layout>
      <h1 className="text-2xl text-white font-bold mb-6">
        Threat Analytics
      </h1>

      <PieChart width={400} height={300}>
        <Pie
          data={data}
          dataKey="value"
          outerRadius={120}
          label
        >
          {data.map((_, i) => (
            <Cell key={i} fill={COLORS[i]} />
          ))}
        </Pie>
        <Tooltip />
      </PieChart>
    </Layout>
  );
}
