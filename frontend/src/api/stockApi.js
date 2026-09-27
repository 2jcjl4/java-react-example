import { request } from "./client.js";

export function adjustStock(itemId, movementType, quantity, note) {
  return request(`/stock/items/${itemId}/movements`, {
    method: "POST",
    body: { movementType, quantity, note }
  });
}

export function fetchMovements(itemId) {
  return request(`/stock/movements${itemId ? `?itemId=${itemId}` : ""}`);
}
