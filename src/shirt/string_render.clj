(ns shirt.string-render
  (:import [java.awt Graphics Font Color]))

(defn normalize [xs x]
  (/ x (reduce + xs)))

(def fancy-hash (memoize hash))
(defn candidate-partition [s y height i]
  (let [num-partitions (if (< (count s) 40) 1 (rand-int 4))
        partition-offsets (range (count s))
        used-offsets (take num-partitions (sort-by #(fancy-hash [i %]) partition-offsets))
        partitions (map #({:string take (- %2 %1) (drop %1 s)
                           :importance (rand)})
                        (cons 0 used-offsets)
                        (conj (drop 1 used-offsets) (count s)))]
    (for [p partitions]
      (merge
       p
       {:y (+ y (* height (partial normalize (map :importance partitions))))}))))

(defn candidate-goodness [c]
  0)

(defn best-partitions [s width height ^Font f]
  (let [candidates (for [i (range 2)] (candidate-partition s width height f i))]
    (println candidates)
    (first (sort-by candidate-goodness candidates))))

(defn render-string-inside-rectangle [s
                                      ^Graphics graphics
                                      ^Font f
                                      left-x
                                      right-x
                                      top-y
                                      bottom-y]
  (let [g graphics
        partitions (best-partitions s (- right-x left-x) (- top-y bottom-y) f)]
    (.setColor g Color/BLACK)
    (for [{:keys [line size y]} partitions]
      (do
        (.setFont g (.deriveFont f size))
        (.drawString g line left-x y)))))
