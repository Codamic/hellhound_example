(ns hh-example.system
  (:require
   [hellhound.http       :as http]
   [hellhound.components.webserver :as webserver]))


(def dev-system
  {:components [(webserver/factory http/default-routes)]})
