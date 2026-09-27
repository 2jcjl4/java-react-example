import NavBar from "./NavBar.jsx";

export default function Layout({ children }) {
  return (
    <div className="layout">
      <NavBar />
      <main>{children}</main>
    </div>
  );
}
