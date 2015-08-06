(ns people.parse
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn- infer-delimiter
  "Infer the input delimiter by scanning input for one of the known delimiters."
  [input]
  {:pre [(string? input)]}
  (cond (re-find #"\|" input) \|
        (re-find #","  input) \,
        :else \space))

(defn- make-parser
  "Return an appropriate parser for the given input string."
  [input]
  {:pre [(string? input)]}
  (partial csv/read-csv input :separator (infer-delimiter input)))

(defmulti parse
  "Parse input consisting of newline-terminated records with fields delimited by a pipe, comma, or space.
   Return a collection of records -- each record is a vector of strings, one per field."
  class)

(defmethod parse String
  [input]
  (doall ((make-parser input))))

(defmethod parse java.io.Reader
  [input]
  (parse (slurp input)))
