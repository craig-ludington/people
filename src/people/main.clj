(ns people.main
  (require [people.tokenize :refer [tokenize]]
           [people.parse :refer [parse]])
  (:gen-class))

(defn -main
  []
  (println "people!"))
