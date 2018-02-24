(ns getting-started.proxy.components.index-loader
  (:require
   [manifold.stream :as stream]
   [hellhound.component :as component]))


(defn response
  [body]
  {:body body :status 200 :headers []})


(defn load-index   ;; <1>
  [output]
  (fn [event]      ;; <2>
    (let [path (System/getProperty "user.dir")  ;; <3>
          file (str path "/index.html")]        ;; <4>
      (println (str "Loading: " file))

      (stream/put!
       output
       (assoc event :index-content (slurp file)))))) ;; <5>

(defn start!
  [this context]
  (let [[input output] (component/io this)]
    (stream/consume (load-index output) input)   ;; <6>
    this))

(defn stop [this] this)

(defn factory
  []
  (component/make-component ::job start! stop))  ;; <7>
