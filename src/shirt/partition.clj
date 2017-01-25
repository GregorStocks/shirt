(ns shirt.partition
  (:require [clojure.string :as string]))

(defn randomly-partition-string [s]
  (let [partition-offsets (filter #(= (nth s %) \ ) (range (count s)))
        num-partitions (rand-int (long (/ (count partition-offsets) 2)))
        used-offsets (sort (cons 0 (take (dec num-partitions)
                                         (shuffle partition-offsets))))
        partitions (map (fn [start end] (string/trim
                                         (apply str
                                                (take (- end start) (drop start s)))))
                        used-offsets
                        (conj (apply vector (drop 1 used-offsets))
                              (count s)))]
    partitions))
