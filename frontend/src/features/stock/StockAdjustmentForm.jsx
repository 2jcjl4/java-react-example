import { useState } from "react";
import Field from "../../components/Field.jsx";

const MOVEMENT_TYPES = ["RECEIPT", "RETURN", "WASTAGE", "ADJUSTMENT_INCREASE", "ADJUSTMENT_DECREASE"];

export default function StockAdjustmentForm({ items, busy, onSubmit }) {
  const [itemId, setItemId] = useState("");
  const [movementType, setMovementType] = useState("RECEIPT");
  const [quantity, setQuantity] = useState("1");
  const [note, setNote] = useState("");

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit({ itemId: Number(itemId), movementType, quantity: Number(quantity), note });
    setQuantity("1");
    setNote("");
  }

  return (
    <form className="panel" onSubmit={handleSubmit}>
      <h3>Record a stock movement</h3>
      <Field label="Item">
        <select value={itemId} onChange={(event) => setItemId(event.target.value)} required>
          <option value="">Select an item</option>
          {items.map((item) => (
            <option key={item.id} value={item.id}>
              {item.sku} - {item.name} ({item.quantityOnHand} on hand)
            </option>
          ))}
        </select>
      </Field>
      <Field label="Movement type">
        <select value={movementType} onChange={(event) => setMovementType(event.target.value)}>
          {MOVEMENT_TYPES.map((type) => (
            <option key={type} value={type}>
              {type.replace("_", " ").toLowerCase()}
            </option>
          ))}
        </select>
      </Field>
      <Field label="Quantity">
        <input
          type="number"
          min="1"
          value={quantity}
          onChange={(event) => setQuantity(event.target.value)}
          required
        />
      </Field>
      <Field label="Note">
        <input value={note} onChange={(event) => setNote(event.target.value)} />
      </Field>
      <div className="actions">
        <button type="submit" disabled={busy}>
          Apply movement
        </button>
      </div>
    </form>
  );
}
