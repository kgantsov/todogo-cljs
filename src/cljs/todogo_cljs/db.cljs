(ns todogo-cljs.db
  (:require [clojure.walk :refer (keywordize-keys)]
            [re-frame.core :as re-frame]
            [ajax.core :refer [GET POST PUT DELETE]]
            [re-frame.core :refer [dispatch]]
            [todogo-cljs.navigation :refer [nav!]]))

(def default-db
  {:name            "re-frame"
   :main-menu-visible false
   :todo-lists      []
   :todos           []
   :todo-list-id    nil
   :todo-list       nil
   :todo-title      ""
   :todo-list-title ""
   :todo-id         nil
   :todo            nil
   :user-login      nil})


(defn api-call [method url data handler]
  (method
    (str "http://localhost:8080" url)
    {:format :json
     :params data
     :headers {:Auth-Token (.getItem (.-localStorage js/window) "token")}
     :handler handler
     :error-handler (fn [r] (cond
                              (< (:status r) 400) (handler r)
                              (= (:status r) 403) (nav! (str "/sign-in"))
                              (> (:status r) 500) (print (:error r))))}))


(defn get-todo-lists []
  (api-call
    GET
    "/api/v1/list/"
    {}
    (fn [data] (dispatch [:set-todo-lists (keywordize-keys data)]))))

(defn get-todo-list [list-id]
  (api-call
    GET
    (str "/api/v1/list/" list-id "/")
    {}
    (fn [data] (dispatch [:set-todo-list (keywordize-keys data)]))))

(defn get-todos [id]
  (api-call
    GET
    (str "/api/v1/list/" id "/todo/")
    {}
    (fn [data] (dispatch [:set-todos (keywordize-keys data)]))))

(defn get-todo [list-id id]
  (api-call
    GET
    (str "/api/v1/list/" list-id "/todo/" id "/")
    {}
    (fn [data] (dispatch [:set-todo (keywordize-keys data)]))))

(defn create-todo-list [data]
  (api-call
    POST
    (str "/api/v1/list/")
    data
    (fn [] (do (dispatch [:set-todo-list-title ""]) (get-todo-lists)))))

(defn create-todo [list-id data]
  (api-call
    POST
    (str "/api/v1/list/" list-id "/todo/")
    data
    (fn [] (do (dispatch [:set-todo-title ""]) (get-todos list-id)))))

(defn toggle-todo [todo]
  (api-call
    PUT
    (str "/api/v1/list/" (:todo_list_id todo) "/todo/" (:id todo) "/")
    {:title (:title todo)
     :completed (if (= (:completed todo) true) false true)
     :note (:note todo)}
    (fn [] (get-todos (:todo_list_id todo)))))

(defn update-todo [todo]
  (api-call
    PUT
    (str "/api/v1/list/" (:todo_list_id todo) "/todo/" (:id todo) "/")
    todo
    (fn [] (get-todos (:todo_list_id todo)))))

(defn sign-in [data]
  (api-call
    POST
    (str "/api/v1/auth/login/")
    data
    (fn [resp-data] (do (.setItem (.-localStorage js/window) "token", (:token (keywordize-keys resp-data)))
                        (re-frame/dispatch [:set-user-login nil])
                        (nav! (str "/"))))))

(defn delete-todo-list [todo-list-id]
  (api-call
    DELETE
    (str "/api/v1/list/" todo-list-id "/")
    {}
    (fn [] (get-todo-lists))))

(defn delete-todo [todo-list-id todo-id]
  (api-call
    DELETE
    (str "/api/v1/list/" todo-list-id "/todo/" todo-id "/")
    {}
    (fn [] (get-todos todo-list-id))))
