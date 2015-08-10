(ns people.api
  (:require [ring.util.response :refer [response status]]
            [clojure.tools.logging :as log]
            [people.store :refer [store fetch]]
            [people.tokenize :refer [tokenize]]
            [people.parse :refer [parse]]
            [people.report :refer [json-report]]))

(defn by-gender    [] (json-report 1))
(defn by-birthdate [] (json-report 2))
(defn by-name      [] (json-report 3))

(defn records
  [req]
  (let [record (get-in req [:params :people-record])
        parsed (and record (-> record tokenize parse))]
    (log/debug (str "POST /records: record: \"" record "\" parsed: " parsed))
    (if (and (not (empty? parsed)) (store parsed))
      (do (log/debug (str "POST /records: record: \"" record "\" parsed: " parsed))
          (response (json-report parsed 1)))
      (-> (response [{:status :error
                      :reason (cond (not record)    "Missing parameter: people-record"
                                    (empty? record) "Missing value for parameter: people-record"
                                    (empty? parsed) (str "Can not parse parameter: people-record, value: \"" record "\"")
                                    :else           "Unknown error")}])
          (status 400)))))
