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
            [todogo-cljs.navigation :refer [nav!]]
            [todogo-cljs.components :as c]))


(defn todo-lists-panel []
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])
        lists (re-frame/subscribe [:todo-lists])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       (c/nav-bar @main-menu-visible)
       [:div {:class "container"}
        [:div {:class "col-12 "}
         (c/bread-crumbs
           [[:span [:span {:class "fa fa-lg fa-home"}]]])]
        [:div {:class "col-lg-4 col-md-4"}
         (c/create-todo-list-form @todo-list-title)
         (c/todo-lists @lists)]]
       c/footer])))

(defn todo-list-panel []
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])
        lists (re-frame/subscribe [:todo-lists])
        todo-list (re-frame/subscribe [:todo-list])
        todos (re-frame/subscribe [:todos])
        todo-text (re-frame/subscribe [:todo-title])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       (c/nav-bar @main-menu-visible)
       [:div {:class "container"}
        [:div {:class "col-12 "}
         (c/bread-crumbs
           [[:a {:href "#/"} [:span {:class "fa fa-lg fa-home"}]]
            [:span (:title @todo-list)]])]
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}
         (c/create-todo-list-form @todo-list-title)
         (c/todo-lists @lists)]
        [:div {:class "col-lg-4 col-md-4"}
         (c/create-todo-form @todo-list @todo-text)
         (c/todos @todos)]
        [:div {:class "col-lg-4 col-md-4 hidden-sm hidden-xs"}]]
       c/footer])))


(defn todo-panel []
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])
        todo (re-frame/subscribe [:todo])
        todo-list (re-frame/subscribe [:todo-list])
        lists (re-frame/subscribe [:todo-lists])
        todos (re-frame/subscribe [:todos])
        todo-text (re-frame/subscribe [:todo-title])
        todo-list-title (re-frame/subscribe [:todo-list-title])]
    (fn []
      [:div {:class "base-container"}
       (c/nav-bar @main-menu-visible)
       [:div {:class "container"}
        [:div {:class "col-12 "}
         (c/bread-crumbs
           [[:a {:href "#/"} [:span {:class "fa fa-lg fa-home"}]]
            [:a {:href (str "#/lists/" (:id @todo-list))} (:title @todo-list)]
            [:span (:title @todo)]])]
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
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])]
    [:div {:class "base-container"}
     (c/nav-bar @main-menu-visible)
     [:div {:class "container"}
      [:h2 "Sign in page"]
      [:div [:a {:href "#/"} "go to the home page"]]]
     c/footer]))

(defn sign-in-panel []
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])
        user-login (re-frame/subscribe [:user-login])]
    [:div {:class "base-container"}
     (c/nav-bar @main-menu-visible)
     [:div {:class "container"}
      (c/sign-in-form @user-login)]
     c/footer]))

(defn sign-up-panel []
  (let [main-menu-visible (re-frame/subscribe [:main-menu-visible])
        user-login (re-frame/subscribe [:user-login])]
    [:div {:class "base-container"}
     (c/nav-bar @main-menu-visible)
     [:div {:class "container"}
      (c/sign-up-form @user-login)]
     c/footer]))


(defn- panels [panel-name]
  (case panel-name
    :sign-up-panel [sign-up-panel]
    :sign-in-panel [sign-in-panel]
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
