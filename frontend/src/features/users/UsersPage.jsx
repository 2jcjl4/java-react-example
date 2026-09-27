import { useState } from "react";
import Message from "../../components/Message.jsx";
import { createUser, fetchUsers, resetPassword, updateUser } from "../../api/usersApi.js";
import { useApiResource } from "../../hooks/useApiResource.js";
import UserForm from "./UserForm.jsx";
import UserTable from "./UserTable.jsx";

export default function UsersPage() {
  const { data, error, loading, reload, setError } = useApiResource(() => fetchUsers(), []);
  const [busy, setBusy] = useState(false);
  const [notice, setNotice] = useState(null);

  async function run(action, successMessage) {
    setBusy(true);
    setError(null);
    setNotice(null);
    try {
      await action();
      setNotice(successMessage);
      await reload();
      return true;
    } catch (cause) {
      setError(cause.message);
      return false;
    } finally {
      setBusy(false);
    }
  }

  function handleResetPassword(user) {
    const newPassword = window.prompt(`New password for ${user.username} (at least 8 characters)`);
    if (!newPassword) {
      return;
    }
    run(() => resetPassword(user.id, newPassword), `Password updated for ${user.username}`);
  }

  return (
    <section>
      <h2>Users</h2>
      <Message text={error} />
      <Message kind="success" text={notice} />

      <UserForm
        busy={busy}
        onSubmit={(values) => run(() => createUser(values), `Created ${values.username}`)}
      />

      {loading ? (
        <p className="loading">Loading...</p>
      ) : (
        <UserTable
          users={data ?? []}
          onChangeRole={(user, role) =>
            run(
              () => updateUser(user.id, { fullName: user.fullName, role, active: user.active }),
              `${user.username} is now ${role}`
            )
          }
          onToggleActive={(user) =>
            run(
              () => updateUser(user.id, { fullName: user.fullName, role: user.role, active: !user.active }),
              `${user.username} updated`
            )
          }
          onResetPassword={handleResetPassword}
        />
      )}
    </section>
  );
}
