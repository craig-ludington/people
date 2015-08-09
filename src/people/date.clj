(ns people.date
  (:require [clj-time.coerce :refer [from-string]]
            [clj-time.format :as time :refer [formatter unparse]]
            [clojure.tools.logging :as log]))

(def csv-date-formatter (formatter "MM/dd/yyyy"))

(defn to-csv-date
  "Convert a date to a month/day/year string, e.g. \"12/31/2015\"."
  [d]
  (unparse csv-date-formatter d))

(defn parse-date
  "Liberal date parsing. Accepts most standard formats as well as the \"MM/dd/yyyy\" format used by to-csv-date."
  [d]
  (or (from-string d)
      (try (time/parse csv-date-formatter d)
           (catch java.lang.IllegalArgumentException e
             (log/warn (str "parse-date: Cannot parse " "\"" d "\"."))
             nil))))
