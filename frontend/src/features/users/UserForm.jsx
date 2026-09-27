import { useState } from "react";
import Field from "../../components/Field.jsx";

const ROLES = ["ADMIN", "MANAGER", "CASHIER"];
const EMPTY = { username: "", fullName: "", password: "", role: "CASHIER" };

export default function UserForm({ busy, onSubmit }) {
  const [values, setValues] = useState(EMPTY);

  function update(field, value) {
    setValues((current) => ({ ...current, [field]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const created = await onSubmit(values);
    if (created) {
      setValues(EMPTY);
    }
  }

  return (
    <form className="panel" onSubmit={handleSubmit}>
      <h3>New user</h3>
      <Field label="Username">
        <input
          value={values.username}
          onChange={(event) => update("username", event.target.value)}
          minLength={3}
          required
        />
      </Field>
      <Field label="Full name">
        <input
          value={values.fullName}
          onChange={(event) => update("fullName", event.target.value)}
          required
        />
      </Field>
      <Field label="Temporary password">
        <input
          type="password"
          value={values.password}
          onChange={(event) => update("password", event.target.value)}
          minLength={8}
          required
        />
      </Field>
      <Field label="Role">
        <select value={values.role} onChange={(event) => update("role", event.target.value)}>
          {ROLES.map((role) => (
            <option key={role} value={role}>
              {role}
            </option>
          ))}
        </select>
      </Field>
      <div className="actions">
        <button type="submit" disabled={busy}>
          Create user
        </button>
      </div>
    </form>
  );
}
