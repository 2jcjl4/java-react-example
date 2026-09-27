import { useCallback, useEffect, useState } from "react";

/** Loads data from the API and exposes a reload function for use after a mutation. */
export function useApiResource(loader, dependencies = []) {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const reload = useCallback(() => {
    setLoading(true);
    return loader()
      .then((result) => {
        setData(result);
        setError(null);
      })
      .catch((cause) => setError(cause.message))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, dependencies);

  useEffect(() => {
    reload();
  }, [reload]);

  return { data, error, loading, reload, setError };
}
