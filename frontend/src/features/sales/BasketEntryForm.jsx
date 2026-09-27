import { useState } from "react";
import Field from "../../components/Field.jsx";

export default function BasketEntryForm({ items, onAdd }) {
  const [itemId, setItemId] = useState("");
  const [quantity, setQuantity] = useState("1");

  function handleSubmit(event) {
    event.preventDefault();
    const item = items.find((candidate) => candidate.id === Number(itemId));
    if (!item) {
      return;
    }
    onAdd(item, Number(quantity));
    setItemId("");
    setQuantity("1");
  }

  return (
    <form className="panel" onSubmit={handleSubmit}>
      <h3>Add to basket</h3>
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
      <Field label="Quantity">
        <input
          type="number"
          min="1"
          value={quantity}
          onChange={(event) => setQuantity(event.target.value)}
          required
        />
      </Field>
      <div className="actions">
        <button type="submit">Add</button>
      </div>
    </form>
  );
}
