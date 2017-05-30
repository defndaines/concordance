(defproject concordance "0.1.0-SNAPSHOT"
  :description "A library to tell how often words appear in a text."
  :url "https://github.com/defndaines/concordance"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]]
  :aot [concordance.core]
  :main concordance.core)
