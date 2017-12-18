(ns components.webserver-example1
  (:require
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   ;; HellHound's webserver component
   [hellhound.components.webserver :as web]
   ;; HellHound's HTTP router
   [hellhound.http.route :as routes]
   ;; HellHound's HTTP handlers helper ns
   [hellhound.http.handlers :as handlers]
   ;; HellHound's websocket namespace
   [hellhound.http.websocket :as ws]
   [manifold.stream :as s]))

;; The HTTP routes definitions
;; Basically you need to provide a http router map to
;; the `webserver` component. You can have any route
;; you want but you have to make sure that you have
;; the HellHound's websocket route in your router.
(def default-routes
  (routes/router
   (routes/expand-routes
    #{{:host "localhost" :scheme :http :port 3000}
      ["/" :get handlers/hello :route-name :home]
      ["/ws" :get (ws/interceptor-factory)]})))

;; System definition.
(def system
  {:components
   ;; Webserver component. The name of this component
   ;; would be `::hellhound.components.webserver/webserver`
   [(web/factory default-routes)
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
