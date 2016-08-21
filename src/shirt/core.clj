(ns shirt.core
  (:gen-class)
  (:use [clojure.tools.cli :as cli]
        [shirt.renderer :as renderer]))

(defn scary-people
  "Go `n` levels deep into the 'Normal people scare me' shirt"
  [n]
  (apply str (loop [i n
              ret "Normal people scare me"
              quotes (if (odd? i) "\"" "'")]
         (if (zero? i)
           ret
           (recur (dec i)
                  (concat ["Normal people wearing "] quotes ret quotes [" shirts scare me"])
                  (case quotes
                    "\"" "'"
                    "'" "\""))))))

(def bit->word {0 "foo"
                1 "bar"})

(defn number->word [i]
  (str (bit->word (rem i 2))
       (let [q (quot i 2)]
         (when (pos? q)
           (number->word q)))))

(def string-styles #{"heredoc" "quotes"})
(def output-formats #{"text" "image"})

(defn scary-heredoc [n]
  (apply str
         (concat
          (for [i (range n 1 -1)]
            (str "Normal people wearing <<" (number->word i) "\n"))
          ["Normal people scare me"]
          (for [i (range 1 n)]
            (str "\n" (number->word (inc i)) "\nshirts scare me")))))
(def cli-options
  [["-o" "--output-format FORMAT" "Output format (text or image)" :default "image" :validate-fn (partial contains? output-formats)]
   ["-n" "--n N" "n for which to render scary(n)" :default 10 :parse-fn #(Long/parseLong %)]
   ["-s" "--string-style STYLE" "String style (heredoc or quotes)" :default "heredoc" :validate-fn (partial contains? string-styles)]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [{:keys [errors options summary]} (cli/parse-opts args cli-options)
        n (:n options)]
    (cond
      (seq errors) (doseq [e errors]
                     (println e))
      (:help options) (println summary)
      :else (renderer/render
             options
             (case (:string-style options)
               "heredoc" (scary-heredoc n)
               "quotes" (scary-people n))))))
