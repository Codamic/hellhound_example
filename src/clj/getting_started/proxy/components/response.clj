(ns getting-started.proxy.components.response
  (:require [hellhound.component :as component]
            [manifold.stream :as stream]))


(defn make-response
  [output]
  (fn [{:keys [index-content] :as event}]
    (stream/put! output
                 (assoc event :response {:body index-content
                                         :headers []
                                         :status 200}))))

(defn ->response
  [this context]
  (let [[input output] (component/io this)]
    (stream/consume (make-response output) input)
    this))

(defn ->response-factory
  []
  (component/make-component ::->response ->response #(identity %))) ;; <1>
