import { request } from "./client.js";

export function recordSale(lines) {
  return request("/sales", { method: "POST", body: { lines } });
}

export function fetchSales() {
  return request("/sales");
}
