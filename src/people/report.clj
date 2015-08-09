(ns people.report
  (require [clojure.data.csv :as csv]
           [clj-time.coerce :refer [to-string]]
           [clj-time.format :refer [formatter unparse]]
           [people.date :refer [to-csv-date]]
           [people.store :refer [fetch]]))

(defn gender-last-name-ascending
  [records]
  (sort-by (fn [m] (str (:gender m) (:last-name m))) records))

(defn date-of-birth-ascending
  [records]
  (sort-by (fn [m] (:date-of-birth m)) records))

(defn last-name-descending
  [records]
  (reverse (sort-by :last-name records)))

(defn to-csv
  "Return a CSV formatted report from records."
  [records]
  (with-out-str
    (csv/write-csv *out* (map (fn [{:keys [last-name first-name gender favorite-color date-of-birth]}]
                                [last-name first-name gender favorite-color (to-csv-date date-of-birth)])
                              records))))
(defn native-report
  "Return a sorted report of the given kind."
  ([records kind]
   {:pre [(every? map? records) (#{1 2 3} kind)]}
   (condp = kind
     1 (gender-last-name-ascending records)
     2 (date-of-birth-ascending records)
     3 (last-name-descending records)))
  ([kind] (native-report (fetch) kind)))

(defn print-report
  "Return a sorted CSV report of the given kind."
  ([records kind]
   (to-csv (native-report records kind)))
  ([kind] (print-report (fetch) kind)))

(defn json-report
  "Report with dates serialized for JSON."
  ([records kind]
   (map (fn [{dob :date-of-birth :as rec}] (assoc rec :date-of-birth (to-string dob))) (native-report records kind)))
  ([kind] (json-report (fetch) kind)))

