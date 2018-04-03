(ns kafka.consumer-example1
  (:require
   [hellhound.components.kafka-consumer :as kafka-consumer]
   [hellhound.component :as component]
   [hellhound.system :as system]
   [manifold.stream :as s]))

(def kconfig {"bootstrap.servers" "localhost:9092"
              "group.id"          "test"})


(defn print-start
  [this context]
  (let [[in out] (component/io this)]
    (s/consume #(fn [record]
                  (println "Got a Record:")
                  (println record))
               in))
  this)

(defn print-stop
  [this]
  this)

(def kafka-system
  {:components [(kafka-consumer/factory kconfig ["sometopic"])
                (component/make-component ::topic-print
                                          print-start
                                          print-stop)]
   :workflow   [(:hellhound.components/kafka-consumer ::topic-print)]})


(defn main
  [& args]
  (system/set-system! kafka-system)
  (system/start!))
