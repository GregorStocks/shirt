(ns shirt.partition)

(defn randomly-partition-string [s]
  (let [num-partitions (rand-int 2 40)
        partition-offsets (range (count s))
        used-offsets (sort (cons 0 (take (dec num-partitions)
                                         (shuffle partition-offsets))))
        partitions (map (fn [start end] (apply str (take (- end start) (drop start s))))
                        used-offsets
                        (conj (apply vector (drop 1 used-offsets))
                              (count s)))]))
