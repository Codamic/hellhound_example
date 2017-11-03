(ns components.webserver-example1
  (:require
   [io.pedestal.http.route :as router]
   [hellhound.system :as system]
   [hellhound.component :as hcomp]
   [hellhound.components.webserver :as web]
   [hellhound.http.route :as routes]
   [manifold.stream :as s]))

(def default-routes
  (routes/router
   (router/expand-routes
                  #{{:host "localhost" :scheme :http :port 3000}
                    ["/" :get `routes/hello :route-name :home]})))
     ;;["/ws" :get [(routes/ws-interceptor (:input context) (:output context))] :route-name :ws]}))
(defn default
  [context]
  (fn [req]
    (let [matched (io.pedestal.http.route.router/find-route default-routes (assoc req :path-info (:uri req)))
          matched-fn (:interceptors matched)]
      (if matched
        (do
          (println "Returned: " (keys (io.pedestal.interceptor.chain/execute (assoc context :req req) matched-fn)))
          (:response (io.pedestal.interceptor.chain/execute (assoc context :req req) matched-fn)))
        {:status 404 :headers {} :body "Note found"}))))



(def system
  {:components [(web/factory default)]})

(defn main
  []
  (system/set-system! system)
  (system/start!)
  (let [output (hcomp/output (system/get-component :hellhound.components.webserver/webserver))]
    (s/consume #(println "Received: " %) output)))
