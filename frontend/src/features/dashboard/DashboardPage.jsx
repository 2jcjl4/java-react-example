import Message from "../../components/Message.jsx";
import { fetchDashboard, fetchLowStock } from "../../api/reportsApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import LowStockTable from "./LowStockTable.jsx";
import SummaryCards from "./SummaryCards.jsx";

export default function DashboardPage() {
  const { data, error, loading } = useApiResource(
    () => Promise.all([fetchDashboard(), fetchLowStock()]),
    []
  );

  if (loading) {
    return <p className="loading">Loading...</p>;
  }
  if (error) {
    return <Message text={error} />;
  }

  const [summary, lowStock] = data;

  return (
    <section>
      <h2>Dashboard</h2>
      <SummaryCards summary={summary} />
      <h3>Items needing a reorder</h3>
      <LowStockTable items={lowStock} />
    </section>
  );
}
