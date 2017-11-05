(ns components.webserver-example1
  (:require
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   [hellhound.components.webserver :as web]
   [hellhound.http.route :as routes]
   [hellhound.http.handlers :as handlers]
   [manifold.stream :as s]))

(def default-routes
  (routes/router
   (routes/expand-routes
                  #{{:host "localhost" :scheme :http :port 3000}
                    ["/" :get handlers/hello :route-name :home]})))
     ;;["/ws" :get [(routes/ws-interceptor (:input context) (:output context))] :route-name :ws]}))

(def system
  {:components [(web/factory default-routes)]})

(defn main
  []
  (system/set-system! system)
  (system/start!)
  (let [output (hcomp/output (system/get-component :hellhound.components.webserver/webserver))]
    (s/consume #(println "Received: " %) output)))
