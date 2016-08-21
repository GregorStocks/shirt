(ns shirt.core
  (:gen-class))


(defn scary-people
  "Go `n` levels deep into the 'Normal people scare me' shirt"
  [n]
  (loop [i n
         ret "Normal people scare me"
         quotes (if (odd? i) \" \')]
    (if (zero? i)
      ret
      (recur (dec i)
             (str "Normal people wearing " quotes ret quotes " shirts scare me")
             (case quotes
               \" \'
               \' \")))))

(defn -main [& args]
  (doseq [n (range (read-string (or (first args) "10")))]
    (println (scary-people n))))
