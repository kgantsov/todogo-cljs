(ns todogo-cljs.db
  (:require [clojure.walk :refer (keywordize-keys)]
            [ajax.core :refer [GET POST PUT DELETE]]
            [re-frame.core :refer [dispatch]]))

(def default-db
  {:name            "re-frame"
   :todo-lists      []
   :todos           []
   :todo-list-id    nil
   :todo-list       nil
   :todo-title      ""
   :todo-list-title ""
   :todo-id         nil
   :todo            nil})


(defn get-todo-lists []
  (GET "http://localhost:8080/api/v1/list/"
       {:handler (fn [data] (dispatch [:set-todo-lists (keywordize-keys data)]))}))

(defn get-todo-list [list-id]
  (GET (str "http://localhost:8080/api/v1/list/" list-id "/")
       {:handler (fn [data] (dispatch [:set-todo-list (keywordize-keys data)]))}))

(defn get-todos [id]
  (GET (str "http://localhost:8080/api/v1/list/" id "/todo/")
       {:handler (fn [data] (dispatch [:set-todos (keywordize-keys data)]))}))

(defn get-todo [list-id id]
  (GET (str "http://localhost:8080/api/v1/list/" list-id "/todo/" id "/")
       {:handler (fn [data] (dispatch [:set-todo (keywordize-keys data)]))}))

(defn create-todo-list [ data]
  (POST (str "http://localhost:8080/api/v1/list/")
        {:format        :json
         :params        data
         :handler       (fn [] (do (dispatch [:set-todo-list-title ""])
                                            (get-todo-lists)))
         :error-handler (fn [r] (prn r))}))

(defn create-todo [list-id data]
  (POST (str "http://localhost:8080/api/v1/list/" list-id "/todo/")
        {:format        :json
         :params        data
         :handler       (fn [] (do (dispatch [:set-todo-title ""])
                                            (get-todos list-id)))
         :error-handler (fn [r] (prn r))}))

(defn toggle-todo [todo]
  (PUT (str "http://localhost:8080/api/v1/list/" (:todo_list_id todo) "/todo/" (:id todo) "/")
        {:format        :json
         :params        {:title (:title todo)
                         :completed (if (= (:completed todo) true) false true)
                         :note (:note todo)}
         :handler       (fn [] (get-todos (:todo_list_id todo)))
         :error-handler (fn [r] (prn r))}))

(defn update-todo [todo]
  (PUT (str "http://localhost:8080/api/v1/list/" (:todo_list_id todo) "/todo/" (:id todo) "/")
        {:format        :json
         :params        todo
         :handler       (fn [] (get-todos (:todo_list_id todo)))
         :error-handler (fn [r] (prn r))}))

(defn delete-todo-list [todo-list-id]
  (DELETE (str "http://localhost:8080/api/v1/list/" todo-list-id "/")
          {:handler (fn [] (get-todo-lists))}))

(defn delete-todo [todo-list-id todo-id]
  (DELETE (str "http://localhost:8080/api/v1/list/" todo-list-id "/todo/" todo-id "/")
          {:handler (fn [] (get-todos todo-list-id))}))
