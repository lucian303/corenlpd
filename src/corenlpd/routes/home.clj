(ns corenlpd.routes.home
  (:import
    (java.io StringReader StringWriter)
    (edu.stanford.nlp.pipeline Annotation StanfordCoreNLP))
  (:require [corenlpd.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn parse [text]
  (let [an (Annotation. text)
        pipeline (StanfordCoreNLP.)
        output (StringWriter.)]
    (.annotate pipeline an)
    (.xmlPrint pipeline an output)
    (.toString output)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/parse" []
    (fn [req]
     (let [text (get (:params req) :text)]
                (parse text)))))
