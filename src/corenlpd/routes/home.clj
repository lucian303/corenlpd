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

(defroutes home-routes
  (GET* "/parse" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text through Core NLP."
    (fn [req]
     (let [text (get (:params req) :text)
           xml (parser/parse text)]
           (-> xml
              response
              (content-type "text/xml"))))))
