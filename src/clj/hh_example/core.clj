(ns hh-example.core
  (:require
   [hh-example.system :as system]
   [hellhound.system  :as hellhound])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (hellhound/set-system! system/dev-system)
  (hellhound/start!))
