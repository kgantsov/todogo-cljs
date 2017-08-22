(ns todogo-cljs.events
  (:require [re-frame.core :as re-frame]
            [todogo-cljs.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 :set-main-menu-visible
 (fn [db [_ main-menu-visible]]
   (assoc db :main-menu-visible main-menu-visible)))

(re-frame/reg-event-db
  :set-todo-lists
 (fn [db [_ todo-lists]]
   (assoc db :todo-lists todo-lists)))

(re-frame/reg-event-db
  :set-todo-list
 (fn [db [_ todo-list]]
   (assoc db :todo-list todo-list)))

(re-frame/reg-event-db
 :set-todos
 (fn [db [_ todos]]
   (assoc db :todos todos)))

(re-frame/reg-event-db
 :set-todo-list-id
 (fn [db [_ todo-list-id]]
   (assoc db :todo-list-id todo-list-id)))

(re-frame/reg-event-db
 :set-todo-id
 (fn [db [_ todo-id]]
   (assoc db :todo-id todo-id)))

(re-frame/reg-event-db
 :set-todo
 (fn [db [_ todo]]
   (assoc db :todo todo)))

(re-frame/reg-event-db
 :set-user-login
 (fn [db [_ user-login]]
   (assoc db :user-login user-login)))

(re-frame/reg-event-db
 :set-form-errors
 (fn [db [_ form-errors]]
   (assoc db :form-errors form-errors)))

(re-frame/reg-event-db
  :set-todo-title
  (fn [db [_ todo-title]]
   (assoc db :todo-title todo-title)))

(re-frame/reg-event-db
  :set-todo-list-title
  (fn [db [_ todo-list-title]]
   (assoc db :todo-list-title todo-list-title)))

(re-frame/reg-event-db
  :set-display-completed
  (fn [db [_ display-completed]]
   (assoc db :display-completed display-completed)))
