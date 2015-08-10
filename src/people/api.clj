(ns people.api
  (:require [ring.util.response :refer [redirect response]]
            [clojure.tools.logging :as log]
            [people.store :refer [store fetch]]
            [people.tokenize :refer [tokenize]]
            [people.parse :refer [parse]]
            [people.report :refer [json-report]]))

(defn by-gender    [] (json-report 1))
(defn by-birthdate [] (json-report 2))
(defn by-name      [] (response (json-report 3)))

(defn records
  [req]
  (let [record (get-in req [:params :people-record])]
    (log/debug (str "POST /records: record: \"" record "\""))
    (store (-> record tokenize parse))
    (log/debug (str "records: (fetch) " (fetch) )))
  (redirect "/"))
