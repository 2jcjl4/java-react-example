const ROLES = ["ADMIN", "MANAGER", "CASHIER"];

export default function UserTable({ users, onChangeRole, onToggleActive, onResetPassword }) {
  return (
    <table>
      <thead>
        <tr>
          <th>Username</th>
          <th>Full name</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {users.map((user) => (
          <tr key={user.id}>
            <td>{user.username}</td>
            <td>{user.fullName}</td>
            <td>
              <select value={user.role} onChange={(event) => onChangeRole(user, event.target.value)}>
                {ROLES.map((role) => (
                  <option key={role} value={role}>
                    {role}
                  </option>
                ))}
              </select>
            </td>
            <td>{user.active ? "Active" : "Disabled"}</td>
            <td className="actions">
              <button type="button" onClick={() => onToggleActive(user)}>
                {user.active ? "Disable" : "Enable"}
              </button>
              <button type="button" onClick={() => onResetPassword(user)}>
                Reset password
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
