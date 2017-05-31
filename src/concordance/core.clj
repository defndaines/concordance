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

(def lexicographic-order
  "Map comparator for ordering keys in dictionary order."
  ;; String keys will order alphabetically without any assistance.
  identity)

(defn frequency-then-lexicographic-order
  "Map comparator for ordering by largest values first, then alphabetically
  when values are the same."
  [[k v]]
  [(- v) k])

(defn -main [& args]
  (prn (format "args=%s" args)))
