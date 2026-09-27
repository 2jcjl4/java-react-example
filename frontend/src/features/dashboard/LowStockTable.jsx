export default function LowStockTable({ items }) {
  if (items.length === 0) {
    return <p>Every item is above its reorder level.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>SKU</th>
          <th>Name</th>
          <th>Category</th>
          <th>On hand</th>
          <th>Reorder level</th>
          <th>Shortfall</th>
        </tr>
      </thead>
      <tbody>
        {items.map((item) => (
          <tr key={item.id}>
            <td>{item.sku}</td>
            <td>{item.name}</td>
            <td>{item.category}</td>
            <td>{item.quantityOnHand}</td>
            <td>{item.reorderLevel}</td>
            <td>{item.shortfall}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
