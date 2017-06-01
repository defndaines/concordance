(ns concordance.core
  "A library to tell how often words appear in a text."
  (:require [clojure.string :as string]
            [concordance.cli :as cli])
  (:gen-class))


;;; Word Count Algorithm

(defn word-count
  "Get the word count frequencies from a given string."
  [line]
  (-> line
      .toLowerCase
      (string/split #"[^\p{Alnum}']+")
      frequencies))


;;; Sorting Options

(def lexicographic-order
  "Map comparator for ordering keys in dictionary order."
  ;; String keys will order alphabetically without any assistance.
  identity)


(defn frequency-then-lexicographic-order
  "Map comparator for ordering by largest values first, then alphabetically
  when values are the same."
  [[k v]]
  [(- v) k])


(defn sort-fn
  "Get a map comparator sorting function based upon the keyword."
  [order]
  (case order
    "freq" frequency-then-lexicographic-order
    ;; Default to alphabetical order.
    lexicographic-order))


;;; Command Line

(defn exit
  "Exit after reporting a message."
  [status message]
  (println message)
  (System/exit status))


(defn -main [& args]
  (let [{:keys [file-name sort-order exit-message ok?]}
        (cli/validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (let [order (sort-fn sort-order)
            counts (sort-by order (word-count (slurp file-name)))]
        (doseq [[word freq] counts]
          (println (str word " " freq)))))))
