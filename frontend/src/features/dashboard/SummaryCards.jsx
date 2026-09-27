import { formatCurrency } from "../../utils/format.js";

export default function SummaryCards({ summary }) {
  const cards = [
    { label: "Active items", value: summary.activeItems },
    { label: "Low stock", value: summary.lowStockItems },
    { label: "Units on hand", value: summary.totalUnitsOnHand },
    { label: "Stock value", value: formatCurrency(summary.stockValue) },
    { label: "Sales today", value: summary.salesToday },
    { label: "Revenue today", value: formatCurrency(summary.revenueToday) }
  ];

  return (
    <div className="cards">
      {cards.map((card) => (
        <div className="card" key={card.label}>
          <span className="card-label">{card.label}</span>
          <span className="card-value">{card.value}</span>
        </div>
      ))}
    </div>
  );
}
