(defproject concordance "0.2.0-SNAPSHOT"
  :description "A library to tell how often words appear in a text."
  :url "https://github.com/defndaines/concordance"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/tools.cli "0.3.5"]]
  :aot [concordance.core]
  :uberjar-name "concordance.jar"
  :main concordance.core)
