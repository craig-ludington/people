(ns people.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [people.handler :refer :all]
            [people.store :as store]
            [people.parse :refer [parse]]))

(deftest test-app
  (testing "main route redirects to /index.html"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 302))
      (is (re-find #"/index.html" (get-in response [:headers "Location"])))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "GET routes exist"
    (every? identity (map #(let [response (app (mock/request :get %))]
                             (is (= (:status response) 200)))
                          ["/records/gender" "/records/birthdate" "/records/name"]))))

(deftest test-queries
  (reset! store/data #{})
  (store/store (parse [["A" "Allen" "M" "Red"    "04/04/2000"]
                       ["B" "Bob"   "M" "Blue"   "03/03/2000"]
                       ["C" "Carla" "F" "Green"  "02/02/2000"]
                       ["D" "Dot"   "F" "Orange" "01/01/2000"]]))
  ;; Output 1 – sorted by gender (females before males) then by last name ascending.
  (testing "gender query"
    (let [{:keys [status body] :as response} (app (mock/request :get "/records/gender"))]
      (is (= (:status response) 200))
      (let [m (json/read-str (slurp body) :key-fn keyword)]
        (is (= (map :last-name m) ["C" "D" "A" "B"])))))

  ;; Output 2 – sorted by birth date, ascending.
  (testing "birth date query"
    (let [{:keys [status body] :as response} (app (mock/request :get "/records/birthdate"))]
      (is (= (:status response) 200))
      (let [m (json/read-str (slurp body) :key-fn keyword)]
        (is (= (map :last-name m) ["D" "C" "B" "A"])))))

  ;; Output 3 – sorted by last name, descending.
  (testing "name query"
    (let [{:keys [status body] :as response} (app (mock/request :get "/records/birthdate"))]
      (is (= (:status response) 200))
      (let [m (json/read-str (slurp body) :key-fn keyword)]
        (is (= (map :last-name m) ["D" "C" "B" "A"]))))))

(deftest test-post
  (testing "post a record"
    (let [{:keys [status body] :as response} (app (mock/request :post "/records" {:people-record "Doe,John,M,Blue,01/31/2015"}))]
      (is (= (:status response) 200))
      (let [val (json/read-str (slurp body) :key-fn keyword)]
        (is (= val [{:last-name "Doe" :first-name "John" :gender "M" :favorite-color "Blue" :date-of-birth "2015-01-31T00:00:00.000Z"}])))))
  (testing "post with no people-record parameter"
    (let [{:keys [status body] :as response} (app (mock/request :post "/records" {:invalid-parameter "Doe,John,M,Blue,01/31/2015"}))]
      (is (= (:status response) 400))
      (let [val (json/read-str (slurp body) :key-fn keyword)]
        (is (= val [{:status "error", :reason "Missing parameter: people-record"}])))))
  (testing "post with ill-formatted record"
    (let [{:keys [status body] :as response} (app (mock/request :post "/records" {:people-record "Doe,John,M,Blue,Not/A/Date"}))]
      (is (= (:status response) 400))
      (let [val (json/read-str (slurp body) :key-fn keyword)]
        (is (= val [{:status "error" :reason "Can not parse parameter: people-record, value: \"Doe,John,M,Blue,Not/A/Date\""}]))))))
