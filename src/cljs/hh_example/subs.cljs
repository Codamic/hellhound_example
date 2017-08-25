(ns hh-example.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :lang
 (fn [db]
   (:lang db)))

(re-frame/reg-sub
 :recent-articles
 (fn [db]
   (:recent-articles db)))

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 :selected-article
 (fn [db _]
   (:selected-article db)))

(re-frame/reg-sub
 :section-title
 (fn [db _]
   (:section-title (:nav db))))
