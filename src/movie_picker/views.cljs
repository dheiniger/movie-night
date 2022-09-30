(ns movie-picker.views
  (:require
    [re-frame.core :as re-frame]
    [movie-picker.subs :as subs]))

(defn get-value [v]
  (-> v .-target .-value))

(defn movie-query-section []
  [:div.query-panel
   [:button.btn.btn-outline-light {:label "Pick a movie!" :on-click #(re-frame/dispatch [:select-movie])} "Pick a movie!"]])

(defn movie-answer-section []
  (let [movie-choice (re-frame/subscribe [::subs/random-movie])]
    (if (not (nil? @movie-choice))
      [:div [:h1 "You should watch \"" (:title @movie-choice) "\""]
       [:a {:href (str "https://www.imdb.com/title/" (:id @movie-choice))}
        [:img.movie-image {:src (:image @movie-choice)}]]])))

(defn movie-input []
  [:div [:h1 "Add a movie"]
   [:input {:type        "text"
            :placeholder "Enter a movie title"
            :on-change   #(re-frame/dispatch [:input-movie (get-value %)])}]])

(defn header []
  (let [username (re-frame/subscribe [::subs/username])]
    (prn "username is ")
    [:header
     [:nav
      [:i.bi.bi-house-fill [:a {:href "#home" :on-click #(re-frame/dispatch [:navigate :home])} "Home"]]
      [:i.bi.bi-person-fill [:a {:href "#login" :on-click #(re-frame/dispatch [:navigate :login])} (if (nil? @username) "Login" @username)]]]]))

(defn home-panel []
  [:div.heading
   [:h1 "It's Movie Night!"]
   [:p "Having trouble choosing a movie to watch? Click the button below to have the choice made for you!"]
   [:div
    [movie-query-section]
    [movie-answer-section]]])

(defn login-panel []
  (let [username (atom "")
        password (atom "")]
    [:div.heading
     [:form
      [:div.mb-3
       [:label.form-label.pe-3 {:for "username"} "Username"]
       [:input#username {:on-change #(reset! username (get-value %))}]]
      [:div.mb-3
       [:label.form-label.pe-3 {:for "password"} "Password"]
       [:input#password {:type "password" :on-change #(reset! password (get-value %))}]]]
     [:button.btn.btn-outline-light {:on-click #(re-frame/dispatch [:login @username @password])} "Login"]]))

(def routes
  {:home  home-panel
   :login login-panel})

(defn main-panel []
  (let [state (re-frame/subscribe [::subs/state])
        page (re-frame/subscribe [::subs/page])
        errors (re-frame/subscribe [::subs/error-messages])]
    (if (= :loading @state)
      [:div.spinner-border.text-light
       [:span.visually-hidden]]
      [:div
       [header]
       [(@page routes)]
       (if-not (empty? @errors)
         (map (fn [n] [:div.error.mt-2 n]) @errors))])))
