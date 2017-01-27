(ns shirt.partition
  (:require [clojure.string :as string]))

(defn filterer [config]
  (case (:break-on config)
    "spaces" #(= % \ )
    "anything" (constantly true)))

(defn randomly-partition-string [config s]
  (let [f (filterer config)
        valid-offsets (filter #(f (nth s %))
                              (range (count s)))
        num-partitions (rand-int (long (/ (count valid-offsets) 2)))
        used-offsets (sort (cons 0 (take (dec num-partitions)
                                         (shuffle valid-offsets))))
        partitions (map (fn [start end] (string/trim
                                         (apply str
                                                (take (- end start) (drop start s)))))
                        used-offsets
                        (conj (apply vector (drop 1 used-offsets))
                              (count s)))]
    partitions))
