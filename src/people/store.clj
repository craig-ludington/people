(ns people.store)

(def data (atom #{}))

(defn store
  "Add any new records to storage."
  [xs]
  (swap! data into xs))

(defn fetch
  []
  "Return the set of unique records from storage."
  @data)
