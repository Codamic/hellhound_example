(ns components.webserver-example1
  (:require [hellhound.system :as system]
            [hellhound.component :As hcomp]
            [hellhound.components.webserver :as web]
            [hellhound.http.route :as routes]
            [bidi.bidi :as bidi]))

;; (routes/defroutes default-routes
;;   [context]
;;   ["/" {:get {"/" routes/hello
;;               "ws/" routes/ws}}])

(defn default-routes
  [context]
  (println "<<<<<<<<<<")
  (bidi.ring/make-handler
   ["/" {:get {"/" routes/hello
               "ws/" routes/ws}}]))

(def system
  {:components [(web/factory default-routes)]})

(defn main
  []
  (system/set-system! system)
  (system/start!))
