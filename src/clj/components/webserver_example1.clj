(ns components.webserver-example1
  (:require
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   [hellhound.components.webserver :as web]
   [hellhound.http.route :as routes]
   [hellhound.http.handlers :as handlers]
   [hellhound.http.websocket :as ws]
   [manifold.stream :as s]))

(def default-routes
  (routes/router
   (routes/expand-routes
                  #{{:host "localhost" :scheme :http :port 3000}
                    ["/" :get handlers/hello :route-name :home]
                    ["/ws" :get ws/interceptor]})))

(def system
  {:components [(web/factory default-routes)
                {:hcomp/name :system/output
                 :hcomp/start-fn (fn [component]
                                   (s/consume (hcomp/input component)
                                              #(println "GOT: " %)))
                 :hcomp/stop-fn (fn [component] component)}]})

(defn main
  []
  (system/set-system! system)
  (system/start!)
  (let [output (hcomp/output (system/get-component :hellhound.components.webserver/webserver))]
    (s/consume #(println "Received: " %) output)))
