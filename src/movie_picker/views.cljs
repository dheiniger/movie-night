(ns movie-picker.views
  (:require
    [re-frame.core :as re-frame]
    [movie-picker.subs :as subs]
    [re-com.buttons :as b]
    [re-com.throbber :as t]))

(defn get-value [v]
  (-> v .-target .-value))

(defn movie-query-panel []
  (let [state (re-frame/subscribe [::subs/state])]
    [:div.query-panel
     [b/button {:label "Pick a movie!" :class "btn-primary" :on-click #(re-frame/dispatch [:select-movie])}]
     (if (= :loading @state)
       [:h1 "loading..."])]))

(defn movie-answer-panel []
  (let [movie-choice (re-frame/subscribe [::subs/random-movie])]
    [:h1 "You should watch \"" @movie-choice "\""]))

(defn movie-input []
  [:div [:h1 "Add a movie"]
   [:input {:type        "text"
            :placeholder "Enter a movie title"
            :on-change   #(re-frame/dispatch [:input-movie (get-value %)])}]])

(defn movie-submit [movie]
  [b/button {:class    "btn btn-primary"
             :label    "Add movie!"
             :on-click #(re-frame/dispatch [:save-movie @movie])}])

(defn add-movie-panel []
  (let [movie (re-frame/subscribe [::subs/movie-input])]
    [:div [movie-input]
     [movie-submit movie]]))

(defn header []
  [:header
   [:h1 "It's Movie Night!"]
   [:p  "Having trouble choosing a movie to watch? Click the button below to have the choice made for you!"]])

(defn body []
  [:div
   [movie-answer-panel]
   [movie-query-panel]])

(defn main-panel []
  (let [state (re-frame/subscribe [::subs/state])]
    [:div
     (if (= :loading @state)
       [:h1 "loading..."]
       [:div
        [header]
        [body]])]))


