(ns hh-example.core
  (:require [hellhound.websocket.core :as ws]))


(defn ^:export init []
  (js/console.log "-----")
  (ws/connect "ws://localhost:3000/ws/"))
