import { formatTimestamp } from "../../utils/format.js";

export default function MovementTable({ movements }) {
  if (movements.length === 0) {
    return <p>No stock movements recorded yet.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>When</th>
          <th>Item</th>
          <th>Type</th>
          <th>Change</th>
          <th>Resulting</th>
          <th>Reference</th>
          <th>By</th>
          <th>Note</th>
        </tr>
      </thead>
      <tbody>
        {movements.map((movement) => (
          <tr key={movement.id}>
            <td>{formatTimestamp(movement.occurredAt)}</td>
            <td>
              {movement.sku} - {movement.itemName}
            </td>
            <td>{movement.movementType.replace("_", " ").toLowerCase()}</td>
            <td>{movement.quantityDelta > 0 ? `+${movement.quantityDelta}` : movement.quantityDelta}</td>
            <td>{movement.resultingQuantity}</td>
            <td>{movement.reference ?? ""}</td>
            <td>{movement.performedBy}</td>
            <td>{movement.note ?? ""}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
