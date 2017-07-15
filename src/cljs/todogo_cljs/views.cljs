(ns todogo-cljs.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :refer [dom-node]]
            [todogo-cljs.db :refer [create-todo-list
                                    create-todo
                                    get-todo-lists
                                    get-todos
                                    toggle-todo
                                    delete-todo-list
                                    delete-todo]]
            [todogo-cljs.components :as c]))


(defn todo-lists-panel []
  (let [lists (re-frame/subscribe [:todo-lists])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       c/nav-bar
       [:div {:class "container"}
        [:div {:class "col-lg-4 col-md-4"}
         (c/create-todo-list-form @todo-list-title)
         (c/todo-lists @lists)]]
       c/footer])))

(defn todo-list-panel []
  (let [lists (re-frame/subscribe [:todo-lists])
        todo-list (re-frame/subscribe [:todo-list])
        todos (re-frame/subscribe [:todos])
        todo-text (re-frame/subscribe [:todo-title])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       c/nav-bar
       [:div {:class "container"}
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}
         (c/create-todo-list-form @todo-list-title)
         (c/todo-lists @lists)]
        [:div {:class "col-lg-4 col-md-4"}
         (c/create-todo-form @todo-list @todo-text)
         (c/todos @todos)]
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}]]
       c/footer])))


(defn todo-panel []
  (let [todo (re-frame/subscribe [:todo])
        todo-list (re-frame/subscribe [:todo-list])
        lists (re-frame/subscribe [:todo-lists])
        todos (re-frame/subscribe [:todos])
        todo-text (re-frame/subscribe [:todo-title])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       c/nav-bar
       [:div {:class "container"}
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}
         (c/create-todo-list-form @todo-list-title)
         (c/todo-lists @lists)]
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}
         (c/create-todo-form @todo-list @todo-text)
         (c/todos @todos)]
        [:div {:class "col-lg-4 col-md-4"}
         (c/edit-todo-form @todo)]]
       c/footer])))


(defn about-panel []
  (fn []
    [:div {:class "base-container"}
     c/nav-bar
     [:div {:class "container"}
      [:h2 "About page"]
      [:div [:a {:href "#/"} "go to the home page"]]]
     c/footer]))


(defn- panels [panel-name]
  ;(println ">>>>>>" panel-name (juxt namespace panel-name))
  (case panel-name
    :todo-lists-panel [todo-lists-panel]
    :todo-list-panel [todo-list-panel]
    :todo-panel [todo-panel]
    :about-panel [about-panel]
    [:div]))


(defn show-panel [panel-name]
  [panels panel-name])


(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [show-panel @active-panel])))
