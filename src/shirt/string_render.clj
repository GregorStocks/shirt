(ns shirt.string-render
  (:require [taoensso.tufte :refer [defnp p profiled profile]])
  (:import [java.awt Graphics Font Color]
           java.awt.font.FontRenderContext))

(defn normalize [xs x]
  (/ x (reduce + xs)))

(def fancy-hash (memoize hash))

(defn candidate-partition [s top-y total-height max-width i g]
  (p :candidate-partition
     (let [num-partitions (inc (mod i (+ 3 (/ (count s) 40))))
           partition-offsets (range (count s))
           used-offsets (sort (cons 0 (take (dec num-partitions)
                                            (sort-by #(fancy-hash [i %]) partition-offsets))))
           partitions (map (fn [start end] {:string (apply str (take (- end start) (drop start s)))
                                            :importance (+ 1 (rand))})
                           used-offsets
                           (conj (apply vector (drop 1 used-offsets))
                                 (count s)))]
       (first (reduce
                (fn [[acc y available-height] p]
                  (let [base-height (+ available-height (* total-height
                                                           (normalize (map :importance partitions)
                                                                      (:importance p))))
                        [height width font] (loop [height base-height]
                                              (let [font (Font. "Impact" Font/BOLD (long height))
                                                    fm (.getFontMetrics g font)
                                                    width (.stringWidth fm (:string p))]
                                                (if (< max-width width)
                                                  (recur (* height 0.9))
                                                  [height width font])))]
                    [ (conj acc (merge p
                                       {:height (long height)
                                        :width (long width)
                                        :font font
                                        :top-y (long y)
                                        :bottom-y (+ (long y) (long height))}))
                     (+ y height)
                     (- base-height height)]))
                [[] top-y 0]
                partitions)))))

(def min-height 15)
(defn candidate-badness [^Graphics g max-width max-height c]
  (p :general-badness (+
   ;; the taller the better!!
   (p :height-badness (reduce + (map (comp - #(Math/sqrt %) :height) c)))
   ;; distance from the width of the shirt is bad
   (p :width-coverage-badness (reduce max (map #(Math/abs (- max-width (:width %))) c)))
   (count c)
   (let [total-height (p :total-height (reduce + (map :height c)))]
     (Math/abs (- total-height max-height))))))

(def num-candidates 2500)
(defn best-partitions [s ^Graphics g width y height]
  (p :best-partitions
     (let [candidates (for [i (range num-candidates)] (candidate-partition s y height width i g))]
       (first (p :sort-candidates (sort-by (partial candidate-badness g width height) candidates))))))

(defn render-string-inside-rectangle
  "Tries to render a tall version of the string roughly within the rectangle (it might overflow on the right).
  Returns {:bottom-y y} where y is the y-coordinate of the lowest pixel it touches."
  [s
   ^Graphics graphics
   left-x
   right-x
   top-y
   bottom-y]
  (let [g graphics
        partitions (best-partitions s g (- right-x left-x) top-y (- bottom-y top-y))]
    (.setColor g Color/BLACK)
    (doseq [{:keys [string font height bottom-y] :as p} partitions]
      (.setFont g font)
      (.drawString g string left-x bottom-y))
    {:bottom-y (last (sort (map :bottom-y partitions)))}))
