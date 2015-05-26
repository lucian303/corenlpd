(ns corenlpd.routes.services
  (:require [compojure.core :refer [defroutes]]
            [compojure.api.sweet :refer :all]
            [corenlpd.parser :refer :all]
            [ring.util.response :refer [response content-type]]
            [clj-json [core :as json]]))

(def content-types {:xml "text/xml"
                    :json "application/json"})

(defn send-response [data type]
  (-> data
    response
    (content-type (type content-types))))

(defroutes service-routes
  (GET* "/parse" []
        :return       String
        :query-params [text :- String]
        :summary      "Process some text through Core NLP."
        (send-response (parse text :parse-full) :xml))
  (GET* "/parse-with-pos" []
        :return String
        :query-params [text :- String]
        :summary      "Process some text already tagged with parts of speech through Core NLP."
        (send-response (json/generate-string (parse-with-pos text :englishPCFG)) :json)))
