(ns concordance.core
  "A library to tell how often words appear in a text."
  (:require [clojure.string :as string]
            [concordance.cli :as cli])
  (:import (java.util TreeMap))
  (:gen-class))


;;; Word Count Algorithm

(def inc-count
  "Java BiFunction for incrementing a count in a Map."
  (reify
    java.util.function.BiFunction
    (apply [this k v] (if v (inc v) 1))))


(defn- count-reducer!
  "Reducing function which accumulates word frequencies in a *mutable* TreeMap."
  [^TreeMap acc line]
  (doseq [word (string/split line #"[^\p{Alnum}']+")]
    (if (seq word)  ; Skip blank lines
      (.compute acc (string/lower-case word) inc-count)))
  acc)

(defn word-count
  "Get the word count frequencies from a given sequence of strings."
  [arg]
  (let [lines (if (string? arg) [arg] arg)]
    (into (sorted-map)
          (reduce
            count-reducer!
            (TreeMap.)
            lines))))


;;; Sorting Options

(defn frequency-order
  "Map comparator for ordering by largest values first, then alphabetically
  when values are the same."
  [[k v]]
  [(- v) k])


(defn- sorted
  "Get the word count map sorted in the desired order."
  [order coll]
  (case order
    "freq" (sort-by frequency-order coll)
    ;; Sorted alphabetically by default.
    coll))


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
        (let [counts (word-count (line-seq reader))
              for-printing (sorted sort-order counts)]
          (doseq [[word freq] for-printing]
            (println (str word " " freq))))))))
