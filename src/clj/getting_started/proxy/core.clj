(ns getting-started.proxy.core
  (:require [getting-started.proxy.system :as system]
            [hellhound.system :as hellhound]
            [aleph.http :as http])
  (:gen-class))


(defn -main
  [& args]
  (let [[url _port path] args
        port             (Integer. (or _port "3000"))]

    (if (nil? url)
      (println "URL is missing. Please pass the 3rd party URL as the first arg")
      (let [proxy-system (system/factory port url)]       ;; <1>
        (println (str "Starting server on " port "..."))
        (hellhound/set-system! proxy-system)              ;; <2>
        (hellhound/start!)))))                            ;; <3>
