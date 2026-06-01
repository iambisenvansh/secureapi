export default function StatCard({ title, value, color }) {
  const colors = {
    blue: "border-blue-500 text-blue-400",
    green: "border-green-500 text-green-400",
    yellow: "border-yellow-500 text-yellow-400",
    red: "border-red-500 text-red-400",
  };

  return (
    <div className={`bg-gray-900 border-l-4 ${colors[color]} p-5 rounded-lg`}>
      <p className="text-sm text-gray-400">{title}</p>
      <h2 className="text-3xl font-bold">{value}</h2>
    </div>
  );
}
