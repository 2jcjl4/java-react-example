import { request } from "./client.js";

export function fetchItems({ search = "", includeInactive = false } = {}) {
  const params = new URLSearchParams();
  if (search) {
    params.set("search", search);
  }
  if (includeInactive) {
    params.set("includeInactive", "true");
  }
  const query = params.toString();
  return request(`/items${query ? `?${query}` : ""}`);
}

export function createItem(item) {
  return request("/items", { method: "POST", body: item });
}

export function updateItem(id, item) {
  return request(`/items/${id}`, { method: "PUT", body: item });
}

export function setItemActive(id, active) {
  return request(`/items/${id}/${active ? "activate" : "deactivate"}`, { method: "POST" });
}
