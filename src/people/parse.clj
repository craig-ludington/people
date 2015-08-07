(ns people.parse
  (:require [clojure.data.csv :as csv]))

(defn infer-delimiter
  "Infer the input delimiter by scanning input for one of the known delimiters."
  [input]
  {:pre [(string? input)]}
  (cond (re-find #"\|" input) \|
        (re-find #","  input) \,
        :else \space))

(defn make-tokenizer
  "Return an appropriate tokenizer for the given input string."
  [input]
  {:pre [(string? input)]}
  (partial csv/read-csv input :separator (infer-delimiter input)))

(defmulti tokenize
  "Tokenize input consisting of newline-terminated records with fields delimited by a pipe, comma, or space.
   Return a collection of records -- each record is a vector of strings, one per field."
  class)

(defmethod tokenize String
  [input]
  (doall ((make-tokenizer input))))

(defmethod tokenize java.io.Reader
  [input]
  (tokenize (slurp input)))
