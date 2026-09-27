import { useState } from "react";
import Message from "../../components/Message.jsx";
import { createItem, fetchItems, setItemActive, updateItem } from "../../api/itemsApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import { useAuth } from "../../hooks/useAuth.jsx";
import ItemForm from "./ItemForm.jsx";
import ItemTable from "./ItemTable.jsx";

export default function InventoryPage() {
  const { hasRole } = useAuth();
  const canManage = hasRole("ADMIN", "MANAGER");

  const [search, setSearch] = useState("");
  const [includeInactive, setIncludeInactive] = useState(false);
  const [editing, setEditing] = useState(null);
  const [busy, setBusy] = useState(false);

  const { data, error, loading, reload, setError } = useApiResource(
    () => fetchItems({ search, includeInactive }),
    [search, includeInactive]
  );

  async function run(action) {
    setBusy(true);
    setError(null);
    try {
      await action();
      setEditing(null);
      await reload();
    } catch (cause) {
      setError(cause.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <section>
      <h2>Inventory</h2>

      <div className="toolbar">
        <input
          placeholder="Search by name, SKU or category"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />
        <label>
          <input
            type="checkbox"
            checked={includeInactive}
            onChange={(event) => setIncludeInactive(event.target.checked)}
          />
          Include discontinued
        </label>
      </div>

      <Message text={error} />

      {canManage && (
        <ItemForm
          item={editing}
          busy={busy}
          onCancel={() => setEditing(null)}
          onSubmit={(values) =>
            run(() => (editing ? updateItem(editing.id, values) : createItem(values)))
          }
        />
      )}

      {loading ? (
        <p className="loading">Loading...</p>
      ) : (
        <ItemTable
          items={data ?? []}
          canManage={canManage}
          onEdit={setEditing}
          onToggleActive={(item) => run(() => setItemActive(item.id, !item.active))}
        />
      )}
    </section>
  );
}
