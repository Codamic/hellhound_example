(ns systems.simple-system1
  (:require [hellhound.system :as system :refer [defcomponent]]))

(defn start-fn1
  [component context]
  (println "Starting Component 1...")
  component)

(defn stop-fn1
  [component]
  (println "Stopping component 2...")
  component)

(defn start-fn2
  [component context]
  (println "Starting Component 1...")
  component)

(defn stop-fn2
  [component]
  (println "Stopping component 2...")
  component)

(def component-1 {:hellhound.component/name :simple-system/component-1
                  :hellhound.component/start-fn start-fn1
                  :hellhound.component/stop-fn  stop-fn1})

(def component-2 (defcomponent :simple-system/component-2 start-fn2 stop-fn2 [:simple-system/component-1]))

(def simple-system
  {:components [component-2 component-1]})

(defn main
  []
  (system/set-system! simple-system)
  (system/start!))
