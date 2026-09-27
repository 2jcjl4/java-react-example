import { formatCurrency } from "../../utils/format.js";

export default function BasketTable({ lines, onRemove }) {
  if (lines.length === 0) {
    return <p>The basket is empty.</p>;
  }

  const total = lines.reduce((sum, line) => sum + line.unitPrice * line.quantity, 0);

  return (
    <table>
      <thead>
        <tr>
          <th>Item</th>
          <th>Quantity</th>
          <th>Unit price</th>
          <th>Line total</th>
          <th />
        </tr>
      </thead>
      <tbody>
        {lines.map((line) => (
          <tr key={line.itemId}>
            <td>
              {line.sku} - {line.name}
            </td>
            <td>{line.quantity}</td>
            <td>{formatCurrency(line.unitPrice)}</td>
            <td>{formatCurrency(line.unitPrice * line.quantity)}</td>
            <td>
              <button type="button" onClick={() => onRemove(line.itemId)}>
                Remove
              </button>
            </td>
          </tr>
        ))}
        <tr>
          <td colSpan={3}>
            <strong>Total</strong>
          </td>
          <td>
            <strong>{formatCurrency(total)}</strong>
          </td>
          <td />
        </tr>
      </tbody>
    </table>
  );
}
