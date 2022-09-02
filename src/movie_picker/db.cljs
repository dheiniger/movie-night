(ns movie-picker.db)

(def default-db
  {:state :not-loading
   :page :home
   :username nil
   :movies [{:title "Top Gun"} {:title "Top Gun 2"} {:title "Mrs. Doubtfire"}]
   :selected-movie nil})
