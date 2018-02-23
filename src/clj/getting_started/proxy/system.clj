(ns getting-started.proxy.system
  (:require
   [hellhound.system :as hh-system :refer [make-component]]
   [hellhound.component :as hcomp]
   [getting-started.proxy.components.web :as web]
   [getting-started.proxy.components.crawler :as crawler]
   [getting-started.proxy.components.index-loader :as loader]))

(defn uri
  [event]
  (:uri (:request event)))

(defn factory
  [port host]
  {:components [(web/factory {:port port})
                (crawler/factory host)
                (loader/factory)
                (web/->response-factory)]

   :workflow [[::web/server #(not (= "/" (uri %))) ::crawler/job]
              [::web/server #(= "/" (uri %))       ::loader/job]
              [::crawler/job ::web/server]
              [::loader/job  ::web/->response]
              [::web/->response ::web/server]]})
