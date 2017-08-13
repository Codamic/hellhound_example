(ns hh-example.system
  (:require [aleph.http :as http]))

(defn handler [req]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "hello!"})

(defn aleph-factory
  [handler]
  (fn [this context]
    (println "Running aleph server")
    (assoc this :instance (http/start-server handler {:port 3000}))))

(defn stop-aleph
  [this]
  (println "stopping system")
  (if (:instance this)
    (do
      (.close (:instance this))
      (dissoc this :instance))
    this))


(def webserver {:hellhound.component/name :webserver
                :hellhound.component/start-fn (aleph-factory handler)
                :hellhound.component/stop-nf stop-aleph})

(def dev-system
  {:components [webserver]})
