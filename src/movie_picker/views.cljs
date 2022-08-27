(ns movie-picker.views
  (:require
    [re-frame.core :as re-frame]
    [movie-picker.subs :as subs]))

(defn get-value [v]
  (-> v .-target .-value))

(defn movie-query-panel []
  [:div.query-panel
   [:button {:label "Pick a movie!" :class "btn btn-outline-light" :on-click #(re-frame/dispatch [:select-movie])} "Pick a movie!"]])

(defn movie-answer-panel []
  (let [movie-choice (re-frame/subscribe [::subs/random-movie])]
    (if (not (nil? @movie-choice))
      [:div [:h1 "You should watch \"" (:title @movie-choice) "\""]
       [:a {:href (str "https://www.imdb.com/title/" (:id @movie-choice))}
        [:img {:src (:image @movie-choice)}]]])))

(defn movie-input []
  [:div [:h1 "Add a movie"]
   [:input {:type        "text"
            :placeholder "Enter a movie title"
            :on-change   #(re-frame/dispatch [:input-movie (get-value %)])}]])

(defn add-movie-panel []
  (let [movie (re-frame/subscribe [::subs/movie-input])]
    [:div [movie-input]
     [movie-submit movie]]))

(defn header []
  [:header
   [:nav
    [:a {:href "/home"} "Home"]
    [:a {:href "/login"} "Login"]]
   [:div.heading
    [:h1 "It's Movie Night!"]
    [:p "Having trouble choosing a movie to watch? Click the button below to have the choice made for you!"]]])

(defn body []
  [:div
   [movie-query-panel]
   [movie-answer-panel]])

(defn main-panel []
  (let [state (re-frame/subscribe [::subs/state])]
    (if (= :loading @state)
      [:div.spinner-border.text-light
       [:span.visually-hidden]]
      [:div
       [header]
       [body]])))


