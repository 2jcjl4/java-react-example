import { useState } from "react";
import Message from "../../components/Message.jsx";
import { fetchItems } from "../../api/itemsApi.js";
import { recordSale } from "../../api/salesApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import BasketEntryForm from "./BasketEntryForm.jsx";
import BasketTable from "./BasketTable.jsx";

export default function PointOfSalePage() {
  const items = useApiResource(() => fetchItems(), []);
  const [lines, setLines] = useState([]);
  const [error, setError] = useState(null);
  const [confirmation, setConfirmation] = useState(null);
  const [busy, setBusy] = useState(false);

  function addLine(item, quantity) {
    setLines((current) => {
      const existing = current.find((line) => line.itemId === item.id);
      if (existing) {
        return current.map((line) =>
          line.itemId === item.id ? { ...line, quantity: line.quantity + quantity } : line
        );
      }
      return [
        ...current,
        { itemId: item.id, sku: item.sku, name: item.name, unitPrice: Number(item.unitPrice), quantity }
      ];
    });
  }

  function removeLine(itemId) {
    setLines((current) => current.filter((line) => line.itemId !== itemId));
  }

  async function checkout() {
    setBusy(true);
    setError(null);
    setConfirmation(null);
    try {
      const sale = await recordSale(lines.map((line) => ({ itemId: line.itemId, quantity: line.quantity })));
      setLines([]);
      setConfirmation(`Sale ${sale.reference} recorded. Stock has been updated.`);
      await items.reload();
    } catch (cause) {
      setError(cause.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <section>
      <h2>Till</h2>
      <Message text={error ?? items.error} />
      <Message kind="success" text={confirmation} />

      <BasketEntryForm items={items.data ?? []} onAdd={addLine} />

      <h3>Basket</h3>
      <BasketTable lines={lines} onRemove={removeLine} />

      <div className="actions">
        <button type="button" onClick={checkout} disabled={busy || lines.length === 0}>
          {busy ? "Recording..." : "Complete purchase"}
        </button>
      </div>
    </section>
  );
}
