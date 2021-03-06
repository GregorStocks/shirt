(ns shirt.string-render
  (:require [taoensso.tufte :refer [defnp p profiled profile]]
            [shirt.partition :as partition])
  (:import [java.awt Graphics Font Color GraphicsEnvironment RenderingHints]
           java.awt.font.FontRenderContext))

(defn normalize [xs x]
  (/ x (reduce + xs)))

(def fancy-hash (memoize hash))

(def all-fonts (.getAvailableFontFamilyNames (GraphicsEnvironment/getLocalGraphicsEnvironment)))
(def font-types [Font/BOLD
                 Font/ITALIC
                 (+ Font/BOLD Font/ITALIC)
                 Font/PLAIN
                 Font/PLAIN
                 Font/PLAIN])

(defn random-font [height]
  (Font. (rand-nth all-fonts) (rand-nth font-types) (long height)))

(defn candidate-partition [config s top-y total-height max-width i g]
  (p :candidate-partition
     (let [partitions (map (fn [s] {:string s
                                    :importance (+ 1 (rand))})
                           (partition/randomly-partition-string config s))]
       (first (reduce
               (fn [[acc y available-height] p]
                 (let [base-height (+ available-height (* total-height
                                                          (normalize (map :importance partitions)
                                                                     (:importance p))))
                       [height width font] (loop [height base-height]
                                             (let [font (random-font height)
                                                   fm (.getFontMetrics g font)
                                                   width (.stringWidth fm (:string p))]
                                               (if (< max-width width)
                                                 (recur (* height 0.9))
                                                 [height width font])))]
                   [(conj acc (merge p
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
  (p :general-badness
     (+
      ;; the taller the better!!
      (p :height-badness (reduce + (map (comp - #(Math/sqrt %) :height) c)))
      ;; distance from the width of the shirt is bad
      (p :width-coverage-badness (reduce max (map #(Math/abs (- max-width (:width %))) c)))
      (count c)
      (let [total-height (p :total-height (reduce + (map :height c)))]
        (Math/abs (- total-height max-height))))))

(def num-candidates 2500)

(defn best-partitions [config s ^Graphics g width y height]
  (let [result (p :best-partitions
                  (let [candidates (for [i (range num-candidates)
                                         :let [candidate (candidate-partition config s y height width i g)]]
                                     [candidate (candidate-badness g width height candidate)])]
                    (ffirst (sort-by last candidates))))]
    result))

(defn render-string-inside-rectangle
  "Tries to render a tall version of the string roughly within the rectangle (it might overflow on the right).
  Returns {:bottom-y y} where y is the y-coordinate of the lowest pixel it touches."
  [config
   s
   ^Graphics graphics
   left-x
   right-x
   top-y
   bottom-y]
  (let [g graphics
        partitions (best-partitions config s g (- right-x left-x) top-y (- bottom-y top-y))]
    (.setRenderingHint g RenderingHints/KEY_TEXT_ANTIALIASING RenderingHints/VALUE_TEXT_ANTIALIAS_GASP)
    (.setColor g Color/BLACK)
    (doseq [{:keys [string font height bottom-y] :as p} partitions]
      (.setFont g font)
      (.drawString g string left-x bottom-y))
    {:bottom-y (last (sort (map :bottom-y partitions)))}))

