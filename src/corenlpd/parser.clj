(ns corenlpd.parser
  (:require [clojure.string :refer [split]]
            [environ.core :refer [env]]
            [raven-clj.core :refer [capture]]
            [raven-clj.interfaces :refer [stacktrace]]
            [taoensso.timbre :as timbre])
  (:import (java.io StringWriter PrintWriter)
    (java.util Properties ArrayList)
    (edu.stanford.nlp.ling TaggedWord)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP)
    (edu.stanford.nlp.parser.lexparser LexicalizedParser)
    (edu.stanford.nlp.trees TreePrint)))

(def annotators-by-type {:parse-full "tokenize, ssplit, pos, lemma, parse"})

(def models {:englishPCFG "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"})

(defn get-props
  "Get a properties object with the right annotators value."
  [type]
  (let [props (Properties.)]
    (.setProperty props "annotators" (type annotators-by-type))
    props))

(defn return-and-log-error
  "Log the error and return the unsuccessful JSON object."
  [e]
  (let [msg "Error running the NLP parser."]
    (timbre/error msg)
    ;; TODO: Change to a timbre appender if needed elsewhere
    (capture (env :sentry-url)
             (-> {:message msg}
                 (stacktrace e ["corenlpd.parser"]))))
  {:success false})

(defn parse
  "Parse some text using Stanford CoreNLP"
  [text type]
  (try
    (let [annot (Annotation. text)
          props (get-props type)
          pipeline (StanfordCoreNLP. props)
          output (StringWriter.)]
      (.annotate pipeline annot)
      (.xmlPrint pipeline annot output)
      (.toString output))
    (catch Exception e (return-and-log-error e))))

(defn parse-with-pos-raw
  "Given a sentence with POS tags, send it to the parser for reparsing."
  [text model]
  (let [parser (. LexicalizedParser loadModel (model models) [])
        tokens (split text #"((\s+\$*)+|/)")
        words (take-nth 2 tokens)
        tags (take-nth 2 (rest tokens))
        tagged-words (map vector words tags)
        sentence (ArrayList.)]
    (dorun (map (fn [[key val]] (.add sentence (TaggedWord. key val))) (seq tagged-words)))
    (let [tree (.parse parser sentence)
          tp (TreePrint. "wordsAndTags, typedDependencies")
          output (StringWriter.)
          pw (PrintWriter. output false)]
    (.printTree tp tree pw)
    (split (.toString output) #"\n\n"))))

(defn parse-words
  "Parse all words into vectors."
  [sentence]
  (let [words (split sentence #"\s+")]
    (map #(split % #"/") words)))

(defn get-dependency
  "Get each dependency as a map in our expected output format."
  [dep]
  {:type (nth dep 1)
   0 {:feature (nth dep 2) :index (Integer/parseInt (nth dep 3))}
   1 {:feature (nth dep 4) :index (Integer/parseInt (nth dep 5))}})

(defn parse-deps
  "Parse dependencies from their parenthesized form into hashmaps."
  [deps]
  (map get-dependency (re-seq #"(?m)^(\S+)\((\S+)-(\d), (\S+)-(\d)\)(\n*)" deps)))

(defn parse-with-pos
  "Reparse text already tagged with slashes and parts of speech to get
  updated relationships. Words and relationships should be seperated by
  a slash '/'. There should be a space between all words and punctuation."
  [text model]
    (try
      (let [[sentence deps] (parse-with-pos-raw text model)]
        {:wordsAndTags (parse-words sentence)
         :typedDependencies (parse-deps deps)
         :success true})
      (catch Exception e (return-and-log-error e))))
