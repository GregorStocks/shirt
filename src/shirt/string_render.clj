(ns shirt.string-render
  (:import [java.awt Graphics Font Color]))

(def fancy-hash (memoize hash))
(defn candidate-partition [s height i]
  (let [num-partitions (if (< (count s) 40) 1 (rand-int 4))
        partition-offsets 
        used-offsets (take num-partitions (sort-by #(fancy-hash [i %]) partition-offsets))]
    {:partitons (map #(take (- %2 %1) (drop %1 s))
                    (cons 0 used-offsets)
                    (conj (drop 1 used-offsets) (count s)))}))

(defn candidate-goodness [c]
  0)

(defn candidate [s width height ^Font f i]
  (let [c (candidate-partition s height i)]
    (merge c {:goodness (candidate-goodness c)})))

(defn lines-and-sizes [s width heightf ]
  (let [candidates (for [i (range 2)] (candidate-partition s width height f i))]
    (println candidates)
    (first (sort-by :goodness candidates))))

(defn render-string-inside-rectangle [s
                                      ^Graphics graphics
                                      ^Font font
                                      left-x
                                      right-x
                                      top-y
                                      bottom-y]
  (let [g (.clone graphics)
        lines-and-sizes  (lines-and-sizes s (- right-x left-x) (- top y bottom-y) f)]
    (.setColor g Color/BLACK)
    (loop [remaining lines-and-sizes
           y top-y]
      (let [{:keys [line size]} (first lines-and-sizes)]
        (.setFont g (.deriveFont font size))
        (.drawString g line left-x y)
        (recur (rest remaining))))))
