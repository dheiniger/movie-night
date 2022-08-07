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
    (:selected-movie db)
    ;(:title(rand-nth (:movies db))))
  ))

(re-frame/reg-sub
  ::movie-input
  (fn [db]
    (prn "current movie input is " (:current-movie-input db))
    (:current-movie-input db)))
