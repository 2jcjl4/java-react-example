const currencyFormatter = new Intl.NumberFormat("en-GB", { style: "currency", currency: "GBP" });

export function formatCurrency(value) {
  return currencyFormatter.format(Number(value ?? 0));
}

export function formatTimestamp(value) {
  return value ? new Date(value).toLocaleString("en-GB") : "";
}
