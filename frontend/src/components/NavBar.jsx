import { NavLink } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.jsx";

const LINKS = [
  { to: "/", label: "Dashboard", roles: ["ADMIN", "MANAGER", "CASHIER"], end: true },
  { to: "/inventory", label: "Inventory", roles: ["ADMIN", "MANAGER", "CASHIER"] },
  { to: "/stock", label: "Stock", roles: ["ADMIN", "MANAGER"] },
  { to: "/till", label: "Till", roles: ["ADMIN", "MANAGER", "CASHIER"] },
  { to: "/sales", label: "Sales", roles: ["ADMIN", "MANAGER"] },
  { to: "/users", label: "Users", roles: ["ADMIN"] }
];

export default function NavBar() {
  const { user, logout, hasRole } = useAuth();

  return (
    <header className="navbar">
      <strong>Grocery Stock</strong>
      <nav>
        {LINKS.filter((link) => hasRole(...link.roles)).map((link) => (
          <NavLink key={link.to} to={link.to} end={link.end}>
            {link.label}
          </NavLink>
        ))}
      </nav>
      <span className="navbar-user">
        {user.fullName} ({user.role})
        <button type="button" onClick={logout}>
          Sign out
        </button>
      </span>
    </header>
  );
}
