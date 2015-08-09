(ns people.parse
  (:require [clj-time.coerce :refer [from-string]]
            [clojure.tools.logging :as log]
            [people.tokenize :refer [tokenize]]
            [people.date :refer [parse-date]]))

(defn valid?
  "Test that record, an array of tokens, is well-formed."
  [record]
  (let [[last-name first-name gender favorite-color date-of-birth] record]
    (and (= 5 (count record))
         last-name first-name gender favorite-color date-of-birth
         (every? string? record)
         (#{"F" "f" "M" "m"} gender)
         (parse-date date-of-birth))))

(defn parse-record
  "Convert a valid record to a map of last-name, first-name, gender, favorite-color, and date-of-birth. Return nil for an invalid record."
  [record]
  (if (valid? record)
    (let [[nl nf g fc d] record]
      {:last-name nl
       :first-name nf
       :gender g
       :favorite-color fc
       :date-of-birth (parse-date d)})
    (do (log/warn (str "parse-record: invalid record: " record)) false)))

(defn parse
  "Parse a collection of records (each is an array of 5 tokens) returning the set of valid records found.  Duplicates and invalid records are removed."
  [records]
  (set (filter identity (map parse-record records))))
