(ns movie-picker.events
  (:require
    [re-frame.core :as re-frame]
    [movie-picker.db :as app-db]
    [movie-picker.props :as props]
    ;[cljs-http.client :as http]
    [day8.re-frame.http-fx :as http-fx]
    [ajax.core :as ajax])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;(defn x []
;  (go (let [request-body {:movies [{:title "Top Gun"}
;                                   {:title "Top Gun 2"}
;                                   {:title "Mrs. Doubtfire"}
;                                   {:title "junk"}]}
;            response (<! (http/post "http://localhost:3001/movies" {:body         (js/JSON.stringify (clj->js request-body))
;                                                                    :content-type :json}))]
;        (println "request - " request-body)
;        (print "response:" response)
;        response)))


(re-frame/reg-event-fx
  ::initialize-db
  (fn [{:keys [event]}]
    (print "initializing db... " event)
    (let [db (second event)
          api-key props/movie-api-key]
      {:db         (assoc db :state :loading)
       :http-xhrio {:method          :get
                    ;;:uri             "http://localhost:3001/movies"
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

(re-frame/reg-event-fx
  :attempt-login
  (fn [db credentials]
    (prn "credentials are " credentials)))

(re-frame/reg-event-db
  :database-initialized
  (fn [db [_ event]]
    (let [movies (:items event)]
      (print "good result - " movies)
      (assoc db :state :not-loading
        :movies movies))))

(re-frame/reg-event-db
  :http-error-occurred
  (fn [db event]
    (print "something bad happened " event)
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
    (prn "Adding movie " (second movie))
    (prn "current movie list " (:movies db))
    (prn "movie to add title is " (second movie))
    (assoc db :movies (conj (:movies db) {:title (second movie)}))))

(re-frame/reg-event-db
  :input-movie
  (fn [db movie]
    (prn "current db before adding movie input " db)
    (assoc db :current-movie-input (second movie))))

(re-frame/reg-event-db
  :navigate
  (fn [db link]
    (prn "Navigating to " (second link))
    (assoc db :page (second link))))

(re-frame/reg-event-db
  :attempt-login
  (fn [db credentials]
    (let [username (first (rest credentials))
          password (second (rest credentials))]
      (prn "logging in... " username password)
      (if (and
            (= "Daniel" username)
            (= "p" password))
        (assoc db :username username :error-messages nil)
        (assoc db :error-messages ["Invalid username - try again"])))))


