import { request } from "./client.js";

export function fetchDashboard() {
  return request("/reports/dashboard");
}

export function fetchLowStock() {
  return request("/reports/low-stock");
}
