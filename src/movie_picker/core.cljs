(ns movie-picker.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [movie-picker.events :as events]
   [movie-picker.views :as views]
   [movie-picker.config :as config]
   [movie-picker.db :refer [default-db]]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db default-db])
  (dev-setup)
  (mount-root))
