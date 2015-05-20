(ns corenlpd.parser
  (:import
    (java.io StringWriter)
    (java.util Properties)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP))
  (:require [compojure.api.sweet :refer [GET*]]))

(def annotators-by-type {:parse-full "tokenize, ssplit, pos, lemma, parse"})

(defn get-props [type]
  (let [props (Properties.)]
    (.setProperty props "annotators" (type annotators-by-type))
    props))

(defn parse [text type]
  "Parse some text using Stanford CoreNLP"
  (let [annot (Annotation. text)
        props (get-props type)
        pipeline (StanfordCoreNLP. props)
        output (StringWriter.)]
    (.annotate pipeline annot)
    (.xmlPrint pipeline annot output)
    (.toString output)))
