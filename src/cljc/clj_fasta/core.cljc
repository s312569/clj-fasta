(ns clj-fasta.core
  (:require [clojure.string :refer [trim split-lines]]
            #?(:clj [clojure.java.io :refer [writer]])
            #?(:clj [biodb.core :as bdb])))

(defn- parse-info
  [re line]
  (try
    (second (re-find re line))
    (catch #?(:clj Exception :cljs js/Object) e
        #?(:clj (println "Error in line: " line)
           :cljs (.log js/console (str "Error in line: " line)))
      (throw e))))

(defn- not-first->?
  [l]
  (not (= \> (first l))))

(defn- tokenise
  [l ap dp]
  (let [rem (drop-while not-first->? (:remaining l))
        y (if (seq rem)
            (cons (first rem)
                  (take-while not-first->? (rest rem))))]
    (if (seq y)
      {:yield {:accession (parse-info ap (first y))
               :description (parse-info dp (first y))
               :sequence (apply str (rest y))}
       :remaining (drop-while not-first->? (rest rem))}
      {:end true})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; api
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn fasta-seq
  "Takes a buffered reader and returns a lazy list of hashmaps
  containing the accession, description and sequence of the fasta
  sequences on the reader. Regular expressions to parse the accession
  and description from the fasta sequence can be specified (default is
  accession being text before the first space and the description text
  after the first space). Ignores all lines prior to the first line
  that starts with '>'. If using in ClojureScript, the 'reader'
  argument is a string and is not lazy."
  [reader & {:keys [acc-parse desc-parse]
             :or {acc-parse #"^>([^\s]+)"
                  desc-parse #">[^\s]+\s+(.+)"}}]
  (let [lines (filter #(not (= "" (trim %))) #?(:clj (line-seq reader)
                                                :cljs (split-lines reader)))]
    (->> {:remaining lines}
         (iterate #(tokenise % acc-parse desc-parse))
         (take-while #(not (contains? % :end)))
         (map :yield)
         (filter #(not (nil? %))))))

(defn fasta-string
  "Takes a fasta sequence and returns a string in fasta format."
  [s]
  (str ">" (:accession s) " " (:description s) \newline
       (->> (partition-all 70 (:sequence s))
            (map #(apply str %))
            (interpose \newline)
            (apply str))))

#?(:clj (defn fasta->file
          "Takes a collection of fasta sequences and writes them to the
  specified file. Sequences can be appended to the file using
  the :append keyword and can be transformed prior to writing using
  the :func keyword."
          [col file & {:keys [append func] :or {append false func fasta-string}}]
          (with-open [w (writer file :append append)]
            (dorun (map #(let [n (func %)]
                           (if n
                             (.write w (str n "\n"))))
                        col)))
          file))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; integration with biodb
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#?(:clj (defmethod bdb/table-spec :fasta
          [q]
          (vector [:accession :text "PRIMARY KEY"]
                  [:description :text]
                  [:sequence :text])))

#?(:clj (defmethod bdb/prep-sequences :fasta
          [q]
          (:coll q)))

#?(:clj (defmethod bdb/restore-sequence :fasta
          [q]
          (dissoc q :type)))

