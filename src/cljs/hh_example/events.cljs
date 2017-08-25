(ns hh-example.events
  (:require
   [re-frame.core        :as re-frame]
   [hellhound.websocket.core :as ws]
   [hellhound.handlers.app-db]
   [hh-example.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :ws-connect
 (fn [db _]
   (js/console.log "connecting")
   (ws/connect "ws://localhost:3000/")
   db))
(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 :set-communication-state
 (fn [db [_ state]]
   (update-in db [:communication :state] (fn [_] state))))

(re-frame/reg-event-db
 :select-article
 (fn [db [_ id]]
   (update-in db [:selected-article] (fn [_] id))))
