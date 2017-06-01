(ns concordance.cli
  "Command-line utilities."
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))


(def cli-options
  [["-s" "--sort ORDER" "Sorting order. Must be one of \"alpha\" or \"freq\"."
    :id :sort
    :default "alpha"
    :validate [#(#{"alpha" "freq"} %) "Must be one of \"alpha\" or \"freq\"."]]
   ["-h" "--help"]])


(defn usage
  "Usage information when help is requested or arguments are missing."
  [options-summary]
  (->> ["Utility for counting the frequency of words in a text."
        ""
        "Usage: concordance [-s ORDER] text.txt"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))


(defn error-message
  "Format command line parsing errors for display."
  [errors]
  (str "Error while parsing your request:\n\n"
       (string/join \newline errors)))


(defn validate-args
  "Validate command line arguments.
  Returns map indicating program should exit or map of arguments to perform a
  word count on."
  [args]
  (let [{:keys [options arguments errors summary]}
        (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-message errors)}

      (and (= 1 (count arguments))
           (.exists (clojure.java.io/as-file (first arguments))))
      {:file-name (first arguments) :sort-order (:sort options)}

      :else
      {:exit-message (usage summary)})))
