(ns hh-example.system
  (:require
   [hellhound.http.route       :as router]
   [hellhound.components.aleph :as aleph]))


(def dev-system
  {:components [(aleph/factory router/routes)]})
