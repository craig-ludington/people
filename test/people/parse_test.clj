(ns people.parse-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [people.parse :refer :all]))

(def expected-tokens [["foo" "bar"]
                      ["fred" "wilma"]])

(def pipe-delimited  "foo|bar\nfred|wilma")
(def comma-delimited "foo,bar\nfred,wilma")
(def space-delimited "foo bar\nfred wilma")
(def no-delimiters   "abcdefg")

(deftest test-input-type-inference
  (testing "recognize pipe delimited"
    (is (= \| (infer-delimiter pipe-delimited))))
  (testing "recognize comma delimited"
      (is (= \, (infer-delimiter comma-delimited))))
  (testing "recognize space delimited"
    (is (= \space (infer-delimiter space-delimited))))
  (testing "unrecognized defaults to space"
    (is (= \space (infer-delimiter no-delimiters)))))

(deftest test-tokenize-string
  (testing "tokenize pipe delimited"
    (is (= expected-tokens (tokenize pipe-delimited))))
  (testing "tokenize comma delimited"
    (is (= expected-tokens (tokenize comma-delimited))))
  (testing "tokenize space delimited"
    (is (= expected-tokens (tokenize space-delimited)))))

(deftest test-tokenize-reader
  (testing "tokenize pipe delimited"
    (is (= expected-tokens (tokenize (io/reader "test/data/input.pipe")))))
  (testing "tokenize comma delimited"
    (is (= expected-tokens (tokenize (io/reader "test/data/input.comma")))))
  (testing "tokenize space delimited"
    (is (= expected-tokens (tokenize (io/reader "test/data/input.space"))))))
