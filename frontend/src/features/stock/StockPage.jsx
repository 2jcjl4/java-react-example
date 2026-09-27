import { useState } from "react";
import Message from "../../components/Message.jsx";
import { fetchItems } from "../../api/itemsApi.js";
import { adjustStock, fetchMovements } from "../../api/stockApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import MovementTable from "./MovementTable.jsx";
import StockAdjustmentForm from "./StockAdjustmentForm.jsx";

export default function StockPage() {
  const [busy, setBusy] = useState(false);
  const items = useApiResource(() => fetchItems(), []);
  const movements = useApiResource(() => fetchMovements(), []);

  async function handleAdjust({ itemId, movementType, quantity, note }) {
    setBusy(true);
    movements.setError(null);
    try {
      await adjustStock(itemId, movementType, quantity, note);
      await Promise.all([items.reload(), movements.reload()]);
    } catch (cause) {
      movements.setError(cause.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <section>
      <h2>Stock</h2>
      <Message text={items.error ?? movements.error} />

      <StockAdjustmentForm items={items.data ?? []} busy={busy} onSubmit={handleAdjust} />

      <h3>Recent movements</h3>
      {movements.loading ? (
        <p className="loading">Loading...</p>
      ) : (
        <MovementTable movements={movements.data ?? []} />
      )}
    </section>
  );
}
