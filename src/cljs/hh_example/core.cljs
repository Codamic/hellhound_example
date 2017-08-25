(ns hh-example.core
  (:require
   [hellhound.logger         :as log]
   [hellhound.core           :as hellhound]
   [hh-example.events]
   [hh-example.subs]
   [hh-example.routes        :as router]
   [hh-example.views         :as views]))

(defn dev-setup [])

(defn ^:export init
  []
  (log/info "Starting HellHound application...")
  (hellhound/init!
   {:router           (router/app-routes)
    :dispatch-events  [:initialize-db]
    :dev-setup        dev-setup
    :main-view        views/main-panel}))
