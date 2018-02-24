(ns getting-started.proxy.system
  (:require
   [getting-started.proxy.components.web :as web]
   [getting-started.proxy.components.crawler :as crawler]
   [getting-started.proxy.components.index-loader :as loader]))

(defn uri     ;; <1>
  [event]
  (:uri (:request event)))

(defn factory
  [port host]
  {:components [(web/factory {:port port})
                (crawler/factory host)
                (loader/factory)
                (web/->response-factory)]

   :workflow [[::web/server #(not (= "/" (uri %))) ::crawler/job]   ;; <2>
              [::web/server #(= "/" (uri %))       ::loader/job]    ;; <3>
              [::crawler/job ::web/server]                          ;; <4>
              [::loader/job  ::web/->response]                      ;; <5>
              [::web/->response ::web/server]]})                    ;; <6>
