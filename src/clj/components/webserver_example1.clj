(ns components.webserver-example1
  (:require
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   ;; HellHound's webserver component
   [hellhound.components.webserver :as web]
   [hellhound.http :as http]
   [manifold.stream :as s]))

;; System definition.
(def system
  {:components
   ;; Webserver component. The name of this component
   ;; would be `::hellhound.components.webserver/webserver`
   ;; We used `hellhound.http/default-routes` as the routes
   ;; definition. But you can provide your own routes as long
   ;; as it contains the hellhound websocket endpoint
   [(web/factory http/default-routes)
    ;; A very simple component which relay messages
    ;; to its output while logging them.
    {::hcomp/name ::output
     ::hcomp/start-fn (fn [component ctx]
                        (s/consume
                         (fn [msg]
                           (println "RECEIVED: " msg)
                           (s/put! (hcomp/output component) msg))
                         (hcomp/input component))

                        component)
     ::hcomp/stop-fn (fn [component] component)}]

   ;; A closed workflow. In this workflow the output of `::output`
   ;; component would be the input of `::web/webserver` component
   ;; while the output of `::web/webserver` would be the input
   ;; of `::output`. The important thing to remember in this example
   ;; is that basically the output of the webserver component is a
   ;; stream of data which receives from clients via websocket.
   ;; other requests to none websocket endpoint handle synchronously
   ;; by pedestal interceptors. In general we highly recommend to avoid
   ;; this situation and implement your views in client side which is
   ;; connected to HellHound server by a websocket
   :workflow [[::web/webserver  ::output]
              [::output ::web/webserver]]})

(defn main
  []
  (system/set-system! system)
  (system/start!))
