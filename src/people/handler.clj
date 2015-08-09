(ns people.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defroutes app-routes
  (GET "/" [] "Hello World!!!")
  (GET "/json" [] {:body {:msg "Hi there!"}})
  (route/not-found "Not Found"))

;;  
(def app
  (-> (wrap-defaults app-routes site-defaults)
      (wrap-restful-format :formats [:json-kw])))
