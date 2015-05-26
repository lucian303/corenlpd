(ns corenlpd.routes.home
  (:require [compojure.core :refer [defroutes GET]]))

(defroutes home-routes
  (GET "/" []
    "corenlpd HTTP REST Server"))
