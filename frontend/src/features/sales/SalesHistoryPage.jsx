import Message from "../../components/Message.jsx";
import { fetchSales } from "../../api/salesApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import { formatCurrency, formatTimestamp } from "../../utils/format.js";

export default function SalesHistoryPage() {
  const { data, error, loading } = useApiResource(() => fetchSales(), []);

  if (loading) {
    return <p className="loading">Loading...</p>;
  }

  return (
    <section>
      <h2>Sales</h2>
      <Message text={error} />
      {(data ?? []).length === 0 ? (
        <p>No sales recorded yet.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Reference</th>
              <th>When</th>
              <th>Sold by</th>
              <th>Items</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {data.map((sale) => (
              <tr key={sale.id}>
                <td>{sale.reference}</td>
                <td>{formatTimestamp(sale.soldAt)}</td>
                <td>{sale.soldBy}</td>
                <td>
                  {sale.lines
                    .map((line) => `${line.quantity} x ${line.itemName}`)
                    .join(", ")}
                </td>
                <td>{formatCurrency(sale.totalAmount)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  );
}
