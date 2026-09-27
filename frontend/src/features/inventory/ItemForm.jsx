import { useEffect, useState } from "react";
import Field from "../../components/Field.jsx";

const EMPTY = { sku: "", name: "", category: "", unitPrice: "0.00", reorderLevel: "0" };

export default function ItemForm({ item, onSubmit, onCancel, busy }) {
  const [values, setValues] = useState(EMPTY);

  useEffect(() => {
    setValues(
      item
        ? {
            sku: item.sku,
            name: item.name,
            category: item.category,
            unitPrice: String(item.unitPrice),
            reorderLevel: String(item.reorderLevel)
          }
        : EMPTY
    );
  }, [item]);

  function update(field, value) {
    setValues((current) => ({ ...current, [field]: value }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit({
      sku: values.sku,
      name: values.name,
      category: values.category,
      unitPrice: Number(values.unitPrice),
      reorderLevel: Number(values.reorderLevel)
    });
  }

  return (
    <form className="panel" onSubmit={handleSubmit}>
      <h3>{item ? `Edit ${item.sku}` : "New item"}</h3>
      <Field label="SKU">
        <input value={values.sku} onChange={(event) => update("sku", event.target.value)} required />
      </Field>
      <Field label="Name">
        <input value={values.name} onChange={(event) => update("name", event.target.value)} required />
      </Field>
      <Field label="Category">
        <input
          value={values.category}
          onChange={(event) => update("category", event.target.value)}
          required
        />
      </Field>
      <Field label="Unit price">
        <input
          type="number"
          step="0.01"
          min="0"
          value={values.unitPrice}
          onChange={(event) => update("unitPrice", event.target.value)}
          required
        />
      </Field>
      <Field label="Reorder level">
        <input
          type="number"
          min="0"
          value={values.reorderLevel}
          onChange={(event) => update("reorderLevel", event.target.value)}
          required
        />
      </Field>
      <div className="actions">
        <button type="submit" disabled={busy}>
          {item ? "Save changes" : "Create item"}
        </button>
        {item && (
          <button type="button" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}
