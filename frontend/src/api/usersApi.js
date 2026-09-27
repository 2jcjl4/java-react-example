import { request } from "./client.js";

export function fetchUsers() {
  return request("/users");
}

export function createUser(user) {
  return request("/users", { method: "POST", body: user });
}

export function updateUser(id, user) {
  return request(`/users/${id}`, { method: "PUT", body: user });
}

export function resetPassword(id, newPassword) {
  return request(`/users/${id}/password`, { method: "PUT", body: { newPassword } });
}
