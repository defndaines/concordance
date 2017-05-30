(ns concordance.core
  (:require [clojure.string :as string])
  (:gen-class))

(defn word-count
  "Get the word count frequencies from a given string."
  [line]
  (-> line
      .toLowerCase
      (string/split #"[^\p{Alnum}']+")
      frequencies))

(defn -main [& args]
  (prn (format "args=%s" args)))
