(ns getting-started.proxy.components.echo
  (:require
   [manifold.stream :as stream]
   [hellhound.component :as hcomp]
   [hellhound.system :refer [make-component]]))

(defn response
  [body]
  {:body body :status 200 :headers []})


(defn echo
  [output]
  (fn [msg]
    (stream/put!
     output
     (assoc msg :response (response "s")))))

(defn start!
  [component context]
  (let [input  (hcomp/input component)
        output (hcomp/output component)]
    (stream/consume (echo output) input)
    component))

(defn stop [this] this)

(defn factory
  []
  (make-component ::thread start! stop))
