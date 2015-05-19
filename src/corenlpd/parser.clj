(ns corenlpd.parser
  (:import
    (java.io StringWriter)
    (java.util Properties)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP))
  (:require [compojure.api.sweet :refer [GET*]]))

(defn get-annotators-by-type [type]
  (case type
    :parse-full "tokenize, ssplit, pos, lemma, parse"
    :parse-with-pos "parse"))

(defn get-props [type]
  (let [props (Properties.)]
    (.setProperty props "annotators" (get-annotators-by-type type))
    props))

(defn parse [text type]
  "Parse some text using Stanford CoreNLP"
  (let [an (Annotation. text)
        props (get-props type)
        pipeline (StanfordCoreNLP. props)
        output (StringWriter.)]
    (.annotate pipeline an)
    (.xmlPrint pipeline an output)
    (.toString output)))
