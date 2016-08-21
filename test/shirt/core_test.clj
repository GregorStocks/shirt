(ns shirt.core-test
  (:require [clojure.test :refer :all]
            [shirt.core :refer :all]
            [shirt.renderer :as r]))

(deftest splut
  (testing "splut"
    (is (= (r/split-long-lines []) []))
    (is (= (r/split-long-lines ["aaa" "bbb"]) ["aaa" "bbb"]))
    (is (= (r/split-long-lines ["aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"]) ["aaaaaaaaaaaaaaaaaaaa" "aaaaaaaaaaaaaaaaaaaa" "aa"]))))
