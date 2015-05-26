(ns corenlpd.routes.home
  (:require [corenlpd.parser :as parser]
            [compojure.core :refer [defroutes]]
            [compojure.api.sweet :refer [GET*]]
            [ring.util.response :refer [response content-type]]))

(defn route-parse [text type]
   (let [xml (parser/parse text type)]
         (-> xml
            response
            (content-type "text/xml"))))

(defroutes home-routes
  (GET* "/parse" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text through Core NLP."
    (fn [req] (route-parse text :parse-full)))

  (GET* "/parse-with-pos" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text already tagged with parts of speech through Core NLP."
    (fn [req] (parser/parse-with-pos text :englishPCFG))))
