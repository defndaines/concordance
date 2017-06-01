(ns concordance.core
  "A library to tell how often words appear in a text."
  (:require [clojure.string :as string]
            [concordance.cli :as cli])
  (:import (java.util TreeMap))
  (:gen-class))


;;; Word Count Algorithm

(defn word-count
  "Get the word count frequencies from a given string."
  [line]
  (-> line
      string/lower-case
      (string/split #"[^\p{Alnum}']+")
      frequencies))


(def inc-count
  "Java BiFunction for incrementing a count in a Map."
  (reify
    java.util.function.BiFunction
    (apply [this k v]
      (if v
        (inc v)
        1))))


(defn- count-reducer!
  "Reducing function which accumulates word frequencies in a *mutable* TreeMap."
  [^TreeMap acc line]
  (doseq [word (string/split line #"[^\p{Alnum}']+")]
    (if (seq word)  ; Skip blank lines
      (.compute acc (string/lower-case word) inc-count)))
  acc)


;;; Sorting Options

(def alphabetical-order
  "Map comparator for ordering keys in dictionary order."
  ;; String keys will order alphabetically without any assistance.
  identity)


(defn frequency-order
  "Map comparator for ordering by largest values first, then alphabetically
  when values are the same."
  [[k v]]
  [(- v) k])


(defn- sort-fn
  "Get a map comparator sorting function based upon the keyword."
  [order]
  (case order
    "freq" frequency-order
    ;; Default to alphabetical order.
    alphabetical-order))


;;; Command Line

(defn- exit
  "Exit after reporting a message."
  [status message]
  (println message)
  (System/exit status))


(defn -main [& args]
  (let [{:keys [file-name sort-order exit-message ok?]}
        (cli/validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (with-open [reader (clojure.java.io/reader file-name)]
        (let [counts (reduce count-reducer! (TreeMap.) (line-seq reader))]
          (if (= "freq" sort-order)
            (doseq [[word freq] (sort-by frequency-order counts)]
              (println (str word " " freq)))
            (doseq [[word freq] counts]
              (println (str word " " freq)))))))))
