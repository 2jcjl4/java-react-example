import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout.jsx";
import RequireRole from "./components/RequireRole.jsx";
import LoginPage from "./features/auth/LoginPage.jsx";
import DashboardPage from "./features/dashboard/DashboardPage.jsx";
import InventoryPage from "./features/inventory/InventoryPage.jsx";
import StockPage from "./features/stock/StockPage.jsx";
import PointOfSalePage from "./features/sales/PointOfSalePage.jsx";
import SalesHistoryPage from "./features/sales/SalesHistoryPage.jsx";
import UsersPage from "./features/users/UsersPage.jsx";
import { useAuth } from "./hooks/useAuth.jsx";

export default function App() {
  const { user, loading } = useAuth();

  if (loading) {
    return <p className="loading">Loading...</p>;
  }

  if (!user) {
    return <LoginPage />;
  }

  return (
    <Layout>
      <Routes>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/inventory" element={<InventoryPage />} />
        <Route
          path="/stock"
          element={
            <RequireRole roles={["ADMIN", "MANAGER"]}>
              <StockPage />
            </RequireRole>
          }
        />
        <Route path="/till" element={<PointOfSalePage />} />
        <Route
          path="/sales"
          element={
            <RequireRole roles={["ADMIN", "MANAGER"]}>
              <SalesHistoryPage />
            </RequireRole>
          }
        />
        <Route
          path="/users"
          element={
            <RequireRole roles={["ADMIN"]}>
              <UsersPage />
            </RequireRole>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Layout>
  );
}
