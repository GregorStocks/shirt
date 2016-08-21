(ns shirt.string-render
  (:import [java.awt Graphics Font Color]))

(defn normalize [xs x]
  (/ x (reduce + xs)))

(def fancy-hash (memoize hash))

(defn candidate-partition [s top-y total-height i]
  (let [num-partitions (if (< (count s) 40)
                         1
                         3)
        partition-offsets (range (count s))
        used-offsets (sort (take num-partitions (sort-by #(fancy-hash [i %]) partition-offsets)))
        partitions (map (fn [start end] {:string (apply str (take (- end start) (drop start s)))
                                         :importance (rand)
                                         :start start
                                         :end end})
                        (cons 0 (apply vector used-offsets))
                        (conj (drop 1 used-offsets) (count s)))]
    (println "uh" num-partitions (count s))

    (second
     (reduce
      (fn [[y acc] p]
        (let [height (* total-height
                        (normalize (map :importance partitions)
                                   (:importance p)))]
          [(+ y height)
           (conj acc (merge p
                            {:height height
                             :y y}))]))
      [top-y []]
      partitions))))

(defn candidate-goodness [^Font f width c]
  (reduce + 0
          (for [{:keys [string height]} c]
            0)))

(defn best-partitions [s width y height ^Font f]
  (let [candidates (for [i (range 2)] (candidate-partition s y height i))]
    (doseq [candidate candidates]
      (println candidate (candidate-goodness f width candidate)))
    (first (sort-by (partial candidate-goodness f width) candidates))))

(defn render-string-inside-rectangle [s
                                      ^Graphics graphics
                                      ^Font f
                                      left-x
                                      right-x
                                      top-y
                                      bottom-y]
  (let [g graphics
        partitions (best-partitions s (- right-x left-x) top-y (- bottom-y top-y) f)]
    (.setColor g Color/BLACK)
    (doseq [{:keys [line size y]} partitions]
      (.setFont g (.deriveFont f size))
      (println "drawin" line size y left-x)
      (.drawString g line left-x y))))
