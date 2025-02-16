;; 231RDB342
;; I mostly relied on github copilot this time, so all of the comments above the functions are initial prompts. 
;; However, I modified the functions to make them work.
(ns sentiment-analysis
  (:require
   [clojure.java.io :as clojure.java.io])
  (:require
   [clojure.string :as clojure.string]))

(defn read-file [filename]
  (slurp filename)) ;; used slurp instead of reader since its more readable

(defn word-count [s]
  (count (re-seq #"\w+" s))) ;; \w+ matches any word character

;; function that reads in sentiment-keywords.txt file into a key value map, where key is word and value is sentiment integer
(defn sentiment-keywords []
  (let [filename "sentiment-keywords.txt"]
    (with-open [rdr (clojure.java.io/reader filename)]
      (into {}
            (doall (map #(let [line (clojure.string/split % #"\s+")] ;; \s+ matches any whitespace character
                           [(first line) (Integer/parseInt (second line))])
                        (line-seq rdr)))))))

;; Set up word frequency map and return it
(defn word-freq [s]
  (reduce
   (fn [m w]
     (update m w (fnil inc 0)))
   {}
   (re-seq #"\w+" s)))

;; calculate the sentiment by looking up a keyword, if it exists in the sentiment-keywords map and summing up the sentiment values
(defn sentiment [s]
  (let [sentiment-keywords (sentiment-keywords)
        word-frequencies (word-freq s)
        total-words (word-count s)]
    (if (zero? total-words)
      0
      (/ (float (reduce
                 (fn [sum [word freq]]
                   (if-let [sentiment-value (get sentiment-keywords word)]
                     (+ sum (* sentiment-value freq))
                     sum))
                 0
                 word-frequencies))
         total-words))))

;; function that judges sentiment value, if is is greater than 0.2, it is positive, if it is less than -0.2, it is negative, otherwise it is neutral
(defn sentiment-judgement [s]
  (let [sentiment-value (sentiment s)]
    (cond
      (> sentiment-value 0.2) "positive"
      (< sentiment-value -0.2) "negative"
      :else "neutral")))

;; print word frequency map sorted by value
(defn print-word-freq [s]
  (doseq [[k v] (sort-by val > (word-freq s))]
    (println k v)))

;; function that reads in a file and calculates the word frequency map to the calculate the sentiment and reads input text file from cli
(defn main []
  (let [filename (first *command-line-args*)]
    (let [text (read-file filename)]
      (println "Word frequency:")
      (print-word-freq text)
      (println "Total word count:")
      (println (word-count text))
      (println "Sentiment:")
      (println (sentiment-judgement text)))))

(main)