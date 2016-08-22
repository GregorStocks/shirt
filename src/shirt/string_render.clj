(ns shirt.string-render
  (:import [java.awt Graphics Font Color]
           java.awt.font.FontRenderContext))

(defn normalize [xs x]
  (/ x (reduce + xs)))

(def fancy-hash (memoize hash))

(defn candidate-partition [s top-y total-height i]
  (let [num-partitions (inc (mod i (+ 3 (/ (count s) 40))))
        partition-offsets (range (count s))
        used-offsets (sort (cons 0 (take (dec num-partitions) (sort-by #(fancy-hash [i %]) partition-offsets))))
        partitions (map (fn [start end] {:string (apply str (take (- end start) (drop start s)))
                                         :importance (+ 1 (rand))})
                        used-offsets
                        (conj (apply vector (drop 1 used-offsets))
                              (count s)))]
    (second
     (reduce
      (fn [[y acc] p]
        (let [height (* total-height
                        (normalize (map :importance partitions)
                                   (:importance p)))]
          [(+ y height)
           (conj acc (merge p
                            {:height (long height)
                             :font (Font. "Impact" Font/BOLD (long height))
                             :y (long y)}))]))
      [top-y []]
      partitions))))

(defn candidate-badness [^Graphics g width c]
  (/ (reduce +
          (for [{:keys [string height font]} c]
            (let [fm (.getFontMetrics g font)
                  text-width (.stringWidth fm string)]
              (if (> text-width width)
                ;; overflow is very bad
                (* 4 (- text-width width))
                ;; underflow is slightly bad
                (- width text-width)))))
     (count c)))

(def num-candidates 5000)
(defn best-partitions [s ^Graphics g width y height]
  (let [candidates (for [i (range num-candidates)] (candidate-partition s y height i))
        result (first (sort-by (partial candidate-badness g width) candidates))]
    (first (sort-by (partial candidate-badness g width) candidates))))

(defn render-string-inside-rectangle [s
                                      ^Graphics graphics
                                      left-x
                                      right-x
                                      top-y
                                      bottom-y]
  (let [g graphics
        partitions (best-partitions s g (- right-x left-x) top-y (- bottom-y top-y))]
    (.setColor g Color/BLACK)
    (doseq [{:keys [string font y height] :as p} partitions]
      (.setFont g font)
      (.drawString g string left-x (+ (long y) height)))))
