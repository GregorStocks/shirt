(ns shirt.renderer)

(defn render-shirt-to-image [config s]
  (spit (:image-name config) "haha jk"))

(defn render [config s]
  (case (:output-format config)
    "text" (println s)
    "image" (render-shirt-to-image config s)))
