(ns shirt.profiling
  (require [shirt
            [core :refer [scary-people scary-heredoc]]
            [renderer :refer [render]]]
           [taoensso.tufte :refer [profile add-basic-println-handler!]]))

(def options 
  [{:n 5000 :s "heredoc" :output-format "text"}
   {:n 5000 :s "quotes" :output-format "text"}
   {:n 2 :s "quotes" :output-format "png" :output-filename "examples/quotes-two.png"}
   {:n 4 :s "quotes" :output-format "png" :output-filename "examples/quotes-four.png"}
   {:n 6 :s "quotes" :output-format "png" :output-filename "examples/quotes-six.png"}
   {:n 8 :s "quotes" :output-format "png" :output-filename "examples/quotes-eight.png"}
   {:n 10 :s "quotes" :output-format "png" :output-filename "examples/quotes-ten.png"}
   {:n 2 :s "heredoc" :output-format "png" :output-filename "examples/heredoc-two.png"}
   {:n 4 :s "heredoc" :output-format "png" :output-filename "examples/heredoc-four.png"}
   {:n 6 :s "heredoc" :output-format "png" :output-filename "examples/heredoc-six.png"}
   {:n 8 :s "heredoc" :output-format "png" :output-filename "examples/heredoc-eight.png"}
   {:n 10 :s "heredoc" :output-format "png" :output-filename "examples/heredoc-ten.png"}])

(add-basic-println-handler! {})

(doseq
  [{:keys [s n] :as opts} options]
  (profile {} (render opts (case s
                             "heredoc" (scary-heredoc n)
                             (scary-people n)))))
