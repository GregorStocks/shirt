(ns shirt.dict-intersect
  (:require
    [clojure
     [set :as s]
     [string :refer [split]]]
    [clojure.java.io :as io]))

(def wordset
  "Load corpus of English words into a set. Kinda nervous about keeping it in memory, though (3.7 MB)."
  (with-open [words (io/reader
                      (io/resource "words.txt"))]
    (into #{} (line-seq words))))

(defn- all-substrings [s]
  (flatten
    (for [n (range 1 (count s))]
      (map #(apply str %)
           (partition n 1 s)))))

(defn word-substrings [s]
  (s/intersection
    wordset
    (into #{}
          (mapcat all-substrings
                  (distinct
                    (split s #" "))))))
