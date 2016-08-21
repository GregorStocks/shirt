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

(def bit->word {0 "foo"
                1 "bar"})

(defn number->word [i]
  (str (bit->word (rem i 2))
       (let [q (quot i 2)]
         (when (pos? q)
           (number->word q)))))

(defn scary-heredoc [n]
  (apply str
         (concat
          (for [i (range n 1 -1)]
            (str "Normal people wearing <<" (number->word i) "\n"))
          ["Normal people scare me"]
          (for [i (range 1 n)]
            (str "\n" (number->word (inc i)) "\nshirts scare me")))))

(defn -main [& args]
  (let [n (read-string (or (first args) "10"))
        f (case (or (second args) "heredoc")
             "heredoc" scary-heredoc
             "quotes" scary-people
             (fn [n] "Unknown command: " (second args)))]
    (println (f n))))
