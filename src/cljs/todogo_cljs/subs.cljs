(ns todogo-cljs.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
  :todo-lists
 (fn [db]
   (:todo-lists db)))

(re-frame/reg-sub
  :todo-list
 (fn [db]
   (:todo-list db)))

(re-frame/reg-sub
  :todos
 (fn [db]
   (:todos db)))

(re-frame/reg-sub
  :todo
 (fn [db]
   (:todo db)))

(re-frame/reg-sub
  :user-login
 (fn [db]
   (:user-login db)))

(re-frame/reg-sub
  :form-errors
 (fn [db]
   (:form-errors db)))

(re-frame/reg-sub
  :todo-list
 (fn [db]
   (:todo-list db)))

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
  :todo-list-id
 (fn [db _]
   (:todo-list-id db)))

(re-frame/reg-sub
  :todo-id
 (fn [db _]
   (:todo-id db)))

(re-frame/reg-sub
  :todo-title
  (fn [db _]
   (:todo-title db)))

(re-frame/reg-sub
  :todo-list-title
  (fn [db _]
   (:todo-list-title db)))

(re-frame/reg-sub
  :main-menu-visible
  (fn [db _]
   (:main-menu-visible db)))
