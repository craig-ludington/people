(ns people.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.util.response :refer [redirect]]
            [people.api :as api]))

(defroutes app-routes
  (POST "/records"           req (api/records req))
  (GET  "/records/gender"    []  (api/gender))
  (GET  "/records/birthdate" []  (api/birthdate))
  (GET  "/records/name"      []  (api/name))
  (GET  "/"                  []  (redirect "/index.html"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-restful-format :formats [:json-kw])))
