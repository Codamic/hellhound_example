(ns components.webserver-example1
  (:require [hellhound.system :as system]
            [hellhound.component :as hcomp]
            [hellhound.components.webserver :as web]
            [hellhound.http.route :as routes]
            [manifold.stream :as s]))

(defn default-routes
  [context]
  (routes/ws (:input context) (:output context)))

(def system
  {:components [(web/factory default-routes)]})

(defn main
  []
  (system/set-system! system)
  (system/start!)
  (let [output (hcomp/output (system/get-component :hellhound.components.webserver/webserver))]
    (s/consume #(println "Received: " %) output)))
