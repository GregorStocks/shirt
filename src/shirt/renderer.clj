(ns shirt.renderer
  (:import [java.awt Color Font])
  (:require [mikera.image.core :as i]
            [clojure.string :as string]))

(def max-line-length 20)
(defn split-long-lines [lines]
  (let [result (map #(apply str %)
                    (mapcat (partial partition-all max-line-length) lines))]
    (println "splut" lines result)
    result))

(defn render-shirt-to-image [s]
  (let [image (i/load-image-resource "shirt.jpg")
        g (i/graphics image)
        top-y 150
        bottom-y 450
        lines (split-long-lines (string/split s #"\n"))
        line-height (long (/ (- bottom-y top-y) (count lines)))]
    (.setColor g Color/BLACK)
    (.setFont g (Font. "Impact" Font/BOLD line-height))
    (doall (map (fn [line offset]
                  (.drawString g line 100
                               (+ top-y (* offset line-height))))
                lines (range)))
    (i/show image :title "nice")))

(defn render [config s]
  (case (:output-format config)
    "text" (println s)
    "image" (render-shirt-to-image s)))
