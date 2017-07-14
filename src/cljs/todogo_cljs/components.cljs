(ns todogo-cljs.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :refer [dom-node]]
            [todogo-cljs.db :refer [create-todo-list
                                    create-todo
                                    toggle-todo
                                    delete-todo-list
                                    delete-todo
                                    update-todo]]
            [todogo-cljs.routes :refer [nav!]]))


(def nav-bar
  [:nav {:class "navbar navbar-inverse navbar-fixed-top"}
   [:div {:class "container"}
    [:div {:class "navbar-header"}
     [:a {:href "#/" :class "navbar-brand"} "TODOGO"]]
    [:div {:id "navbar" :class "collapse navbar-collapse"}
     [:ul {:class "nav navbar-nav"}
      [:li
       [:a {:href "#/about"} "About"]]]]]])


(def footer
  [:footer {:class "footer"}
   [:div {:class "container"}
    [:p {:class "text-muted"} "© 2016 Company, Inc."]]])


(defn todo-input [{:keys [title placeholder on-change]}]
  [:input {:class       "form-control"
           :type        "text"
           :value       title
           :placeholder placeholder
           :on-change   on-change}])

(def todo-edit (with-meta todo-input
                          {:component-did-mount #(.focus (dom-node %))}))


(defn create-todo-list-form [title]
  [:div {:class "form-group"}
   [:form {:on-submit (fn [] (create-todo-list {:title title :completed false :note ""}))}
    [todo-edit {:title title
                :placeholder (str "Create a todo list...")
                :on-change #(re-frame/dispatch [:set-todo-list-title (-> % .-target .-value)])}]]])


(defn create-todo-form [todo-list title]
  [:div {:class "form-group"}
   [:form {:on-submit (fn [] (create-todo (:id todo-list) {:title title :completed false :note ""}))}
    [todo-edit {:title title
                :placeholder (str "Add a todo in a list '" (:title todo-list) "'")
                :on-change #(re-frame/dispatch [:set-todo-title (-> % .-target .-value)])}]]])


(defn edit-todo-form [todo]
   [:form {:on-submit (fn [] (update-todo todo))}
    [:div {:class "form-group"}
     [:input {:class       "form-control"
              :type        "text"
              :value       (:title todo)
              :placeholder (str "New todo title")
              :on-change   #(re-frame/dispatch [:set-todo (assoc todo :title (-> % .-target .-value))])}]]
    [:div {:class "form-group"}
     [:textarea {:class       "form-control"
                 :placeholder (str "Note...")
                 :value       (:note todo)
                 :on-blur     (fn [] (update-todo todo))
                 :on-change   #(re-frame/dispatch [:set-todo (assoc todo :note (-> % .-target .-value))])}]]
    [:div {:class "form-group"}
     [:a {:class "btn btn-success"
          :type :submit
          :on-click (fn [] (update-todo todo))}
      "Save"]
     [:a {:class "button btn btn-danger pull-right"
          :type :submit
          :on-click (fn [] (do (nav! (str "/lists/" (:todo_list_id todo)))
                               (delete-todo (:todo_list_id todo) (:id todo))))}
      [:span {:class "fa fa-lg fa-trash"}]]]])


(defn todo-lists [lists]
  [:ul {:class "list-group"}
   (for [list lists]
     ^{:key (:id list)}
     [:li {:class "list-group-item"}
      [:span {:class "fa fa-lg fa-list-ul"}]
      [:a {:href (str "#/lists/" (:id list))}
       (:title list)]
      [:span {:class "button fa fa-lg fa-trash pull-right"
              :on-click (fn [] (do (nav! (str "/"))
                                   (delete-todo-list (:id list))))}]])])


(defn todos [todos]
  [:ul {:class "list-group"}
   (for [todo todos]
     ^{:key (:id todo)}
     [:li {:class "list-group-item"}
      [:input {:type "checkbox"
               :checked (:completed todo)
               :on-change (fn [x]   (toggle-todo todo))}]
      [:a {:href (str "#/lists/" (:todo_list_id todo) "/todos/" (:id todo))} (:title todo)]
      [:span {:class "button fa fa-lg fa-trash pull-right"
              :on-click (fn [] (do (nav! (str "/lists/" (:todo_list_id todo)))
                                   (delete-todo (:todo_list_id todo) (:id todo))))}]])])
