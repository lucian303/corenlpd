(ns corenlpd.parser
  (:require [clojure.string :refer [split]])
  (:import (java.io StringWriter PrintWriter)
    (java.util Properties ArrayList)
    (edu.stanford.nlp.ling TaggedWord)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP)
    (edu.stanford.nlp.parser.lexparser LexicalizedParser)
    (edu.stanford.nlp.trees TreePrint)))

(def annotators-by-type {:parse-full "tokenize, ssplit, pos, lemma, parse"})

(def models {:englishPCFG "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"})

(defn get-props [type]
  (let [props (Properties.)]
    (.setProperty props "annotators" (type annotators-by-type))
    props))

(defn parse
  "Parse some text using Stanford CoreNLP"
  [text type]
  (let [annot (Annotation. text)
        props (get-props type)
        pipeline (StanfordCoreNLP. props)
        output (StringWriter.)]
    (.annotate pipeline annot)
    (.xmlPrint pipeline annot output)
    (.toString output)))

(defn parse-with-pos
  "Reparse text already tagged with slashes and parts of speech to get
  updated relationships. Words and relationships should be seperated by
  a slash '/'. There should be a space between all words and punctuation."
  [text model]
  (let [parser (. LexicalizedParser loadModel (model models) [])
        tokens (split text #"(\s|/)")
        words (take-nth 2 tokens)
        tags (take-nth 2 (rest tokens))
        tagged-words (zipmap words tags)
        sentence (ArrayList.)]
    (dorun (map (fn [[key val]] (.add sentence (TaggedWord. key val))) (seq tagged-words)))
    (let [tree (.parse parser sentence)
          tp (TreePrint. "wordsAndTags, typedDependencies")
          output (StringWriter.)
          pw (PrintWriter. output false)]
    (.printTree tp tree pw)
    (.toString output))))
