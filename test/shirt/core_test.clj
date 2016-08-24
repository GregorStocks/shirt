(ns shirt.core-test
  (:require [clojure.test :refer :all]
            [shirt.core :refer :all]
            [shirt.renderer :as r]
            [shirt.string-render :as sr]))

(deftest normalize
  (is (= (sr/normalize [1 2 3] 3) 1/2))
  (is (= (sr/normalize [1 1] 1) 1/2))
  (is (= (sr/normalize [0 1] 1) 1))
  (is (= (sr/normalize [0 1] 0) 0)))
