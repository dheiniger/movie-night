(ns movie-picker.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::state
  (fn [db]
    (:state db)))

(re-frame/reg-sub
  ::random-movie
  (fn [db]
    (prn "random movie " db)
    (:selected-movie db)))

(re-frame/reg-sub
  ::movie-input
  (fn [db]
    (prn "current movie input is " (:current-movie-input db))
    (:current-movie-input db)))

(re-frame/reg-sub
  ::page
  (fn [db]
    (prn "Current page is " (:page db))
    (:page db)))

(re-frame/reg-sub
  ::username
  (fn [db]
    (prn "getting username... " (:username db))
    (:username db)))

(re-frame/reg-sub
  ::error-messages
  (fn [db]
    (prn "getting errors... " (:error-messages db))
    (:error-messages db)))
