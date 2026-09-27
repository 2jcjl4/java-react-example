import { useAuth } from "../hooks/useAuth.jsx";

export default function RequireRole({ roles, children }) {
  const { hasRole } = useAuth();
  if (!hasRole(...roles)) {
    return <p className="message message-error">You do not have access to this area.</p>;
  }
  return children;
}
