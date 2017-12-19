(ns components.webserver-example1
  (:require
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   ;; HellHound's webserver component ns.
   [hellhound.components.webserver :as web]
   ;; HellHound's transform component ns.
   [hellhound.components.transform :as transform]
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
    ;; Transform component is a very simple component which redirects
    ;; incoming messages from input stream to output stream and applies
    ;; the given function to each message.
    ;;
    ;; In this case we don't do any transformation. We just log the message.
    (transform/factory ::output
                       (fn [context msg]
                         (println "RECEIVED: " msg)
                         msg))]

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
