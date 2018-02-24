(ns getting-started.proxy.components.crawler
  (:require
   [aleph.http :as http]   ;; <1>
   [manifold.deferred :as d]
   [manifold.stream :as stream]
   [hellhound.component :as component]))



(defn response     ;; <2>
  [res]
  {:status (:status res)
   :headers (:headers res)
   :body (:body res)})

(defn fetch-url
  [url]
  (println "PROXYING '" url "'...")
  (d/chain (http/get url)    ;; <3>
           response))

(defn proxy   ;; <4>
  [output host]
  (fn [event]
    (let [request (:request event)
          url     (str host (:uri request))]
      (stream/put! output                        ;; <5>
                   (assoc event
                          :response
                          (fetch-url url))))))   ;; <6>

(defn start    ;; <7>
  [host]
  (fn [this context]
    (let [[input output] (component/io this)]     ;; <7>
     (stream/consume (proxy output host) input)   ;; <8>
     this)))

(defn stop [this] this)   ;; <9>

(defn factory
  [host]
  (component/make-component ::job (start host) stop))  ;; <10>
