(defproject shirt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-Djava.awt.headless=true"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [net.mikera/imagez "0.10.0"]
                 [com.taoensso/tufte "1.0.2"]]
  :main ^:skip-aot shirt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
