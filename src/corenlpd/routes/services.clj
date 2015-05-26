(ns corenlpd.routes.services
  (:require [compojure.core :refer [defroutes]]
            [compojure.api.sweet :refer :all]
            [corenlpd.parser :as parser]
            [ring.util.response :refer [response content-type]]))

(defn set-xml-content-type [data]
  (-> data
    response
    (content-type "text/xml")))

(defroutes service-routes
  (GET* "/parse" []
        :return       String
        :query-params [text :- String]
        :summary      "Process some text through Core NLP."
        (set-xml-content-type (parser/parse text :parse-full)))

  (GET* "/parse-with-pos" []
        :return       String
        :query-params [text :- String]
        :summary      "Process some text already tagged with parts of speech through Core NLP."
        (parser/parse-with-pos text :englishPCFG)))
