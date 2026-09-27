import { useState } from "react";
import Field from "../../components/Field.jsx";
import Message from "../../components/Message.jsx";
import { useAuth } from "../../hooks/useAuth.jsx";

export default function LoginPage() {
  const { login } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await login(username, password);
    } catch (cause) {
      setError(cause.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="login">
      <form onSubmit={handleSubmit}>
        <h1>Grocery Stock</h1>
        <Field label="Username">
          <input value={username} onChange={(event) => setUsername(event.target.value)} required />
        </Field>
        <Field label="Password">
          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </Field>
        <button type="submit" disabled={submitting}>
          {submitting ? "Signing in..." : "Sign in"}
        </button>
        <Message text={error} />
      </form>
    </div>
  );
}
