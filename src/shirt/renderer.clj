(ns shirt.renderer
  (:import [java.awt Color Font])
  (:require [mikera.image.core :as i]
            [clojure.string :as string]
            [shirt.string-render :as sr]))

(defn render-shirt-to-image [s]
  (let [image (i/load-image-resource "shirt.jpg")
        g (i/graphics image)
        top-y 80
        bottom-y 450
        left-x 140
        right-x 350
        lines (string/split s #"\n")
        line-height (long (/ (- bottom-y top-y) (count lines)))]
    (.setColor g Color/BLACK)
    (doall (reduce
            (fn [[i y] line]
              (let [{:keys [bottom-y]} (sr/render-string-inside-rectangle
                                        line
                                        g
                                        left-x
                                        right-x
                                        y
                                        (+ top-y (* (inc i) line-height)))]
                [(inc i) bottom-y]))
            [0 top-y]
            lines))
    image))

(defn render [config s]
  (case (:output-format config)
    "text" (println s)
    "show" (i/show (render-shirt-to-image s) :title "nice")
    "png" (i/write (render-shirt-to-image s) (:output-filename config) "png")))
