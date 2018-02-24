(ns getting-started.proxy.system
  (:require
   [getting-started.proxy.components.web :as web]
   [getting-started.proxy.components.crawler :as crawler]
   [getting-started.proxy.components.index-loader :as loader]
   [getting-started.proxy.components.response :as response]))

(defn uri     ;; <1>
  [event]
  (:uri (:request event)))

(defn factory
  [port host]
  {:components [(web/factory {:port port})
                (crawler/factory host)
                (loader/factory)
                (response/->response-factory)]

   :workflow [[::web/server #(not (= "/" (uri %))) ::crawler/job]   ;; <2>
              [::web/server #(= "/" (uri %))       ::loader/job]    ;; <3>
              [::crawler/job ::web/server]                          ;; <4>
              [::loader/job  ::response/->response]                 ;; <5>
              [::response/->response ::web/server]]})               ;; <6>
