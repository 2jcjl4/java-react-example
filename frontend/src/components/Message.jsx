export default function Message({ kind = "error", text }) {
  if (!text) {
    return null;
  }
  return <p className={`message message-${kind}`}>{text}</p>;
}
