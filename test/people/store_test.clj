(ns people.store-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [people.store :refer :all]))



(def sample-records #{[1] [2] [3]})
(def more-records   #{[4] [5] [6]})
(def all-records (into sample-records more-records))

(defn setup-empty!
  []
  (reset! data #{}))

(defn setup-full!
  []
  (reset! data all-records))

(deftest test-store
  (setup-empty!)
  (testing "store some records in an empty data store"
    (store sample-records)
    (is (= @data sample-records)))
  (testing "store more records in a non-empty data store"
    (store more-records)
    (is (= @data all-records))))

(deftest test-fetch
  (testing "fetch all the records"
    (setup-full!)
    (is (= (fetch) all-records))))


