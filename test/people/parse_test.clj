(ns people.parse-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [people.parse :refer :all]))

(def good-record ["Doe" "John" "M" "Blue" "2001-02-03"])
(def also-good   ["Doe" "Jane" "F" "Green" "2000-12-31"])
(def too-few     ["Doe" "M" "Blue" "2001-02-03"])
(def too-many    ["Doe" "John" "M" "Blue" "2001-02-03" "Extra"])
(def not-strings [999 "John" "M" "Blue" "2001-02-03"])
(def not-date    ["Doe" "John" "M" "Blue" "20xx-02-03"])
(def not-gender   ["Doe" "John" "X" "Blue" "2001-02-03"])

(deftest test-valid?
  (testing "good record passes"
    (is (valid? good-record)))
  (testing "too few tokens fails"
    (is (not (valid? too-few))))
  (testing "too many tokens fails"
    (is (not (valid? too-many))))
  (testing "non-string tokens fails"
    (is (not (valid? not-strings))))
  (testing "too many tokens fails"
    (is (not (valid? not-date))))
  (testing "unrecognized gender fails"
    (is (not (valid? not-gender)))))

(deftest test-parse-record
  (testing "good record creates a proper map"
    (is (let [{:keys [last-name first-name gender favorite-color date-of-birth]} (parse-record good-record)]
          (and (every? string? [last-name first-name gender favorite-color])
               (isa? (class date-of-birth) org.joda.time.DateTime)))))
  (testing "bad record returns nil"
    (is (not (parse-record too-few)))))

(deftest test-parse
  (testing "unique good records are all returned"
    (is (= 2 (count (parse [good-record also-good])))))
  (testing "duplicate records are removed"
    (is (= 1 (count (parse [good-record good-record])))))
  (testing "invalid records are removed"
    (is (= 2 (count (parse [good-record also-good too-many])))))
  (testing "parse returns maps"
    (is (every? map? (parse [good-record also-good])))))
