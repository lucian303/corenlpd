(ns corenlpd.parser
  (:import
    (java.io StringWriter)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP))
  (:require [compojure.api.sweet :refer [GET*]]))

(defn parse [text]
  "Parse some text using Stanford CoreNLP"
  (let [an (Annotation. text)
        pipeline (StanfordCoreNLP.)
        output (StringWriter.)]
    (.annotate pipeline an)
    (.xmlPrint pipeline an output)
    (.toString output)))
