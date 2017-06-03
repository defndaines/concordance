(ns concordance.core-test
  (:require [clojure.test :refer :all]
            [concordance.core :refer :all]))

(deftest word-count-test
  (testing "Without punctuation."
    (is (= {"fish" 4 "blue" 1 "one" 1 "red" 1 "two" 1}
           (word-count "one fish two fish red fish blue fish"))))
  (testing "With mixed upper and lower case."
    (is (= {"fish" 4 "blue" 1 "one" 1 "red" 1 "two" 1}
           (word-count "One Fish Two Fish Red Fish Blue Fish"))))
  (testing "With punctuation."
    (is (= {"fish" 4 "blue" 1 "one" 1 "red" 1 "two" 1}
           (word-count "One Fish, Two Fish, Red Fish, Blue Fish"))))
  (testing "With apostrophes."
    (is (= {"ain't" 3 "a" 1 "don't" 1 "say" 1 "so" 1 "word" 1}
           (word-count "Ain't ain't a word, so don't say ain't."))))
  (testing "Doesn't track empty lines."
    (is (= {"ain't" 3 "a" 1 "don't" 1 "say" 1 "so" 1 "word" 1}
           (word-count "Ain't ain't a word,

                       so don't say ain't.")))))

(deftest sort-test
  (let [phrase "I read the book \"One Fish, Two Fish, Red Fish, Blue Fish\"
               until I was blue in the face."
        dance (word-count phrase)]
    (testing "Sorted alphabetically by default."
      (is (= `("blue" "book" "face")
             (take 3 (map first dance)))))
    (testing "Sort by frequency"
      (is (= '(["fish" 4] ["blue" 2] ["i" 2] ["the" 2] ["book" 1])
             (take 5 (sort-by frequency-order dance)))))))
