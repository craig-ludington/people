(ns people.parse
  (:require [clj-time.coerce :as time]
            [people.tokenize :refer [tokenize]]))

(defn valid?
  "Test that record, an array of tokens, is well-formed."
  [record]
  (let [[last-name first-name gender favorite-color date-of-birth] record]
    (and (= 5 (count record))
         last-name first-name gender favorite-color date-of-birth
         (every? string? record)
         (#{"F" "f" "M" "m"} gender)
         (time/from-string date-of-birth))))

(defn parse-record
  "Convert a valid record to a map of last-name, first-name, gender, favorite-color, and date-of-birth. Return nil for an invalid record."
  [record]
  (when (valid? record)
    (let [[nl nf g fc d] record]
      {:last-name nl
       :first-name nf
       :gender g
       :favorite-color fc
       :date-of-birth (time/from-string d)})))

(defn parse
  "Parse a collection of records (each is an array of 5 tokens) returning the set of valid records found.  Duplicates and invalid records are removed."
  [records]
  (set (filter identity (map parse-record records))))
