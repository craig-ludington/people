(ns people.report-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clj-time.coerce :refer [from-string]]
            [people.report :refer :all]
            [people.parse :refer [parse]]))

(def records (parse [["Bow" "Juan" "M" "Red" "2001-01-01"]
                     ["Doe" "John" "M" "Blue" "2001-02-03"]
                     ["Dough" "Jane" "F" "Green" "2000-12-31"]
                     ["Crow" "Sheryl" "F" "Orange" "1999-07-11"]]))

(deftest test-sorting
  (testing "gender-last-name-ascending"
    (is (= ["Crow" "Dough" "Bow" "Doe"] (map :last-name (gender-last-name-ascending records)))))
  (testing "date-of-birth-ascending"
    (is (= ["Crow" "Dough" "Bow" "Doe"] (map :last-name (date-of-birth-ascending records)))))
  (testing "last-name-descending"
    (is (= ["Dough" "Doe" "Crow" "Bow"] (map :last-name (last-name-descending records))))))

(deftest test-to-csv-date
  (testing "dates properly formatted"
    (is (= "02/03/2001" (to-csv-date (from-string "2001-02-03"))))))

(deftest test-to-csv
  (testing "to-csv properly formats records"
    (is (= "Doe,John,M,Blue,02/03/2001\nCrow,Sheryl,F,Orange,07/11/1999\nDough,Jane,F,Green,12/31/2000\nBow,Juan,M,Red,01/01/2001\n"
           (to-csv records)))))

