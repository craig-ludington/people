(ns people.parse-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [people.parse :refer :all]))

(def expected-parse  [["foo" "bar"]
                      ["fred" "wilma"]])

(def pipe-delimited  "foo|bar\nfred|wilma")
(def comma-delimited "foo,bar\nfred,wilma")
(def space-delimited "foo bar\nfred wilma")
(def no-delimiters   "abcdefg")

(deftest test-input-type-inference
  (testing "recognize pipe"
    (is (= \| (infer-delimiter pipe-delimited))))
  (testing "recognize csv"
      (is (= \, (infer-delimiter comma-delimited))))
  (testing "recognize space"
    (is (= \space (infer-delimiter space-delimited))))
  (testing "unrecognized defaults to space"
    (is (= \space (infer-delimiter no-delimiters)))))

(deftest test-parse-string
  (testing "parse pipe delimited"
    (is (= expected-parse (parse pipe-delimited))))
  (testing "parse comma delimited"
    (is (= expected-parse (parse comma-delimited))))
  (testing "parse space delimited"
    (is (= expected-parse (parse space-delimited)))))

(deftest test-parse-reader
  (testing "parse pipe delimited"
    (is (= expected-parse (parse (io/reader "test/data/input.pipe")))))
  (testing "parse comma delimited"
    (is (= expected-parse (parse (io/reader "test/data/input.comma")))))
  (testing "parse space delimited"
    (is (= expected-parse (parse (io/reader "test/data/input.space"))))))
