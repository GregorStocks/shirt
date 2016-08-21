(ns shirt.renderer
  (:import [java.awt Color Font])
  (:require [mikera.image.core :as i]))

(defn render-shirt-to-image [s]
  (let [image (i/load-image-resource "shirt.jpg")
        g (i/graphics image)]
    (.setColor g Color/BLACK)
    (.setFont g (Font. "Impact" Font/BOLD 30))
    (.drawString g s 10 25)
    (i/show image :title "nice")))

(defn render [config s]
  (case (:output-format config)
    "text" (println s)
    "image" (render-shirt-to-image s)))
