(ns corenlpd.routes.home
  (:require [corenlpd.parser :as parser]
            [compojure.core :refer [defroutes GET]]
            [compojure.api.sweet :refer [GET*]]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [response content-type]]))

(defn route-parse [request type]
   (let [text (get (:params request) :text)
         xml (parser/parse text type)]
         (-> xml
            response
            (content-type "text/xml"))))

(defroutes home-routes
  (GET* "/parse" []
    :return       String
    :query-params [text :- String]
    :summary      "Process some text through Core NLP."
    (fn [req] (route-parse req :parse-full))))
