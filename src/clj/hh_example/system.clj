(ns hh-example.system
  (:require
   [hellhound.components.aleph :as aleph]))

(defn handler [req]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "hello!"})

(def dev-system
  {:components [(aleph/factory handler)]})
