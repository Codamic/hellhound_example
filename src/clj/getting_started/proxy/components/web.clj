(ns getting-started.proxy.components.web ;; <1>
  (:require [aleph.http :as http]   ;; <2>
            [hellhound.component :as hcomp]
            [manifold.stream :as stream]  ;; <3>
            [manifold.deferred :as d])) ;; <4>


(defn handler       ;; <5>
  [input output]
  (stream/consume #(d/success!     ;; <6>
                    (:response-deferred %)
                    (:response %))
                  input)
  (fn [req]               ;; <7>
    (let [response (d/deferred)]     ;; <8>
      (stream/put! output {:request req    ;; <9>
                           :response-deferred response})
      response)))

(defn start!   ;; <10>
  [port]
  (fn [this context]
    (let [[input output] (hcomp/io this)]    ;; <11>
      (assoc this
             :server
             (http/start-server (handler input output) {:port port})))))   ;; <12>

(defn stop!  ;; <13>
  "Stops the running webserver server."
  [this]
  (if (:server this)
    (do
      (.close (:server this))    ;; <14>
      (dissoc this :server))     ;; <15>
    this))


(defn factory
  [{:keys [port] :as config}] ;; <16>
  (hcomp/make-component ::server (start! port) stop!)) ;; <17>
