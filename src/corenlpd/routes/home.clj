(ns corenlpd.routes.home
  (:import
    (java.io StringReader StringWriter)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP))
  (:require [corenlpd.layout :as layout]
            [corenlpd.parser :as parser]
            [compojure.core :refer [defroutes GET]]
            [compojure.api.sweet :refer [GET*]]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [response content-type]]
            [clojure.java.io :as io]))

(defn route-parse [request type]
   (let [text (get (:params request) :text)
         xml (parser/parse text :parse-full)]
         (-> xml
            response
            (content-type "text/xml"))))

(defroutes home-routes
  (GET* "/parse" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text through Core NLP."
    (fn [req] (route-parse req :parse-full)))


  ; TODO: Unfinished
  (GET* "/parse-with-pos" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text that has parts of speech
                  tagged at the end of each word through Core NLP.
                  Format: Word/POS"
    (fn [req] (route-parse req :parse-with-pos))))
