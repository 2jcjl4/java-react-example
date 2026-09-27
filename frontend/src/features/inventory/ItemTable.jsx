import { formatCurrency } from "../../utils/format.js";

export default function ItemTable({ items, canManage, onEdit, onToggleActive }) {
  if (items.length === 0) {
    return <p>No items match the current filter.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>SKU</th>
          <th>Name</th>
          <th>Category</th>
          <th>Price</th>
          <th>On hand</th>
          <th>Reorder level</th>
          <th>Status</th>
          {canManage && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {items.map((item) => (
          <tr key={item.id} className={item.lowStock ? "row-warning" : undefined}>
            <td>{item.sku}</td>
            <td>{item.name}</td>
            <td>{item.category}</td>
            <td>{formatCurrency(item.unitPrice)}</td>
            <td>{item.quantityOnHand}</td>
            <td>{item.reorderLevel}</td>
            <td>{item.active ? "Active" : "Discontinued"}</td>
            {canManage && (
              <td className="actions">
                <button type="button" onClick={() => onEdit(item)}>
                  Edit
                </button>
                <button type="button" onClick={() => onToggleActive(item)}>
                  {item.active ? "Discontinue" : "Restore"}
                </button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
