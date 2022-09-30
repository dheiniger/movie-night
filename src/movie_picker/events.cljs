(ns movie-picker.events
  (:require
    [re-frame.core :as re-frame]
    [movie-picker.db :as app-db]
    [movie-picker.props :as props]
    [day8.re-frame.http-fx :as http-fx]
    [ajax.core :as ajax])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(re-frame/reg-event-fx
  ::initialize-db
  (fn [{:keys [event]}]
      (print "initializing db... " event)
      (let [db (second event)
            api-key props/movie-api-key]
           {:db         (assoc db :state :loading)
            :fx         [[:dispatch [:load-local-store]]]
            :http-xhrio {:method          :get
                         :uri             (str "https://imdb-api.com/en/API/Top250Movies/" api-key)
                         :timeout         8000
                         :response-format (ajax/json-response-format {:keywords? true})
                         :on-success      [:database-initialized]
                         :on-failure      [:http-error-occurred]}})))

(re-frame/reg-event-fx
  :search-requested
  (fn [{:keys [db]} [_ _]]
      (let [key props/movie-api-key]
           {:db         (assoc db :state :loading)
            :http-xhrio {:method          :get
                         :uri             (str "https://imdb-api.com/en/API/Search/" key "/inception")
                         :timeout         8000
                         :response-format (ajax/json-response-format {:keywords? true})
                         :on-success      [:database-initialized]
                         :on-failure      [:http-error-occurred]}})))

(re-frame/reg-event-db
  :database-initialized
  (fn [db [_ event]]
      (let [movies (:items event)]
           (assoc db :state :not-loading
                  :movies movies))))

(re-frame/reg-event-db
  :http-error-occurred
  (fn [db event]
      (assoc db :state :error
             :error-message (second event))))

(re-frame/reg-event-db
  :select-movie
  (fn [db _]
      (assoc db :selected-movie (rand-nth (:movies db)))))

(re-frame/reg-event-db
  :movie-selected
  (fn [db _]
      (assoc db :state :not-loading)))

(re-frame/reg-event-db
  :save-movie
  (fn [db movie]
      (assoc db :movies (conj (:movies db) {:title (second movie)}))))

(re-frame/reg-event-db
  :input-movie
  (fn [db movie]
      (assoc db :current-movie-input (second movie))))

(re-frame/reg-event-db
  :navigate
  (fn [db link]
      (assoc db :page (second link))))

(re-frame/reg-event-fx
  :login
  (fn [coeffects [_ username password]]
      (let [success? (and (= "Daniel" username) (= "p" password))
            db (if success? (assoc (:db coeffects) :error-messages [""] :username username)
                            (assoc (:db coeffects) :error-messages ["Username and password do not match"]))
            event (if success? [:login-succeeded username])]
           {:db           db
            (first event) (second event)})))

(re-frame/reg-event-fx
  :load-local-store
  [(re-frame/inject-cofx :session-store "username")]
  (fn [coeffects event]
      {:db (assoc (:db coeffects) :username (:session-store coeffects))}))


(re-frame/reg-cofx
  :session-store
  (fn [coeffects session-store-key]
      (assoc coeffects
             :session-store
             (js->clj (.getItem js/sessionStorage session-store-key)))))

;;===========
;;fx-handlers
;;===========
(re-frame/reg-fx
  :login-succeeded
  (fn [username]
      (do
        (prn "login success! username is: " username)
        (.setItem js/sessionStorage "username" username))))