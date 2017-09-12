(ns todogo-cljs.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :refer [dom-node atom]]
            [re-com.core       :refer [single-dropdown datepicker-dropdown]]
            [re-com.dropdown :refer [filter-choices-by-keyword single-dropdown-args-desc]]
            [todogo-cljs.db :refer [create-todo-list
                                    create-todo
                                    toggle-todo
                                    delete-todo-list
                                    delete-todo
                                    update-todo
                                    sign-in
                                    sign-up]]
            [todogo-cljs.navigation :refer [nav!]]
            [todogo-cljs.local-storage :refer [ls-get
                                               ls-del]]))


(defn nav-bar [main-menu-visible]
  (let [menu-class (if (= main-menu-visible true) "collapse navbar-collapse in" "collapse navbar-collapse")]
    [:nav {:class "navbar navbar-inverse navbar-fixed-top"}
     [:div {:class "container"}
      [:div {:class "navbar-header"}
       [:a {:href "#/" :class "navbar-brand"} "TODOGO"]
       [:button {:class "navbar-toggle"
                 :type "button"
                 :on-click #(re-frame/dispatch [:set-main-menu-visible (if (= main-menu-visible true) false true)])}
        [:span {:class "icon-bar"}]
        [:span {:class "icon-bar"}]
        [:span {:class "icon-bar"}]]]
      [:div {:id "navbar-main"
             :class menu-class}
       [:ul {:class "nav navbar-nav"}
        [:li
         [:a {:href "#/about"} "About"]]]
       [:ul {:class "nav navbar-nav navbar-right"}
        [:li
         (if (ls-get "token")
           [:a {:on-click (fn [] (do (ls-del "token")
                                     (nav! "/sign-in")))}
            "Sign out"]
           )]]]]]))


(def footer
  [:footer {:class "footer"}
   [:div {:class "container"}
    [:p {:class "text-muted"} "© 2016 Company, Inc."]]])


(defn bread-crumbs [crumbs]
  [:ul {:class "breadcrumb"}
   (for [crumb crumbs]
     ^{:key crumb}
     [:li crumb])])


(defn create-todo-list-form [title]
  [:div {:class "form-group"}
   [:form {:on-submit (fn [e] (do (create-todo-list {:title title :completed false :note ""})
                                  (.preventDefault e)))}
    [:input {:class       "form-control"
             :type        "text"
             :value       title
             :placeholder (str "Create a todo list...")
             :on-change   #(re-frame/dispatch-sync [:set-todo-list-title (-> % .-target .-value)])}]]])


(defn create-todo-form [todo-list title]
  [:div {:class "form-group"}
   [:form {:on-submit (fn [e] (do (create-todo (:id todo-list) {:title title :completed false :note ""})
                                  (.preventDefault e)))}
    [:input {:class       "form-control"
             :type        "text"
             :value       title
             :placeholder (str "Add a todo in a list '" (:title todo-list) "'")
             :on-change   #(re-frame/dispatch-sync [:set-todo-title (-> % .-target .-value)])}]]])

(def priorities [{:id 1 :label "Irrelevant"}
                 {:id 2 :label "Extra low"}
                 {:id 3 :label "Low"}
                 {:id 4 :label "Normal"}
                 {:id 5 :label "High"}
                 {:id 6 :label "Urgent"}
                 {:id 7 :label "Super urgent"}
                 {:id 8 :label "Immediate"}])

(defn edit-todo-form [todo]
   [:form {:on-submit (fn [e] (do (update-todo todo)
                                  (.preventDefault e)))}
    [:div {:class "form-group"}
     [:input {:class       "form-control"
              :type        "text"
              :value       (:title todo)
              :placeholder (str "New todo title")
              :on-change   #(re-frame/dispatch-sync [:set-todo (assoc todo :title (-> % .-target .-value))])}]]
    [:div {:class "form-group"}
     [:textarea {:class       "form-control"
                 :placeholder (str "Note...")
                 :value       (:note todo)
                 :on-change   #(re-frame/dispatch-sync [:set-todo (assoc todo :note (-> % .-target .-value))])}]]
    [:div {:class "form-group"}
     [single-dropdown
      :choices     priorities
      :model       (:priority todo)
      :title?      true
      :placeholder "Choose a priority"
      :width       "100%"
      :max-height  "400px"
      :filter-box? false
      :on-change   #(re-frame/dispatch-sync [:set-todo (assoc todo :priority %)])]]

    [:div {:class "form-group"}
     [datepicker-dropdown
      :model (:dead_line_at todo)
      :show-today? true
      :format "yyyy-MM-dd"
      :start-of-week 0
      :on-change #(re-frame/dispatch-sync [:set-todo (assoc todo :dead_line_at %)])]]

    [:div {:class "form-group"}
     [:input {:class "btn btn-success"
              :type :submit
              :on-click (fn [e] (do (update-todo todo)
                                    (.preventDefault e)))
              :value "Save"}]
     [:a {:class "button btn btn-danger pull-right"
          :type :submit
          :on-click (fn [e] (do (nav! (str "/lists/" (:todo_list_id todo)))
                                (delete-todo (:todo_list_id todo) (:id todo))
                                (.preventDefault e)))}
      [:span {:class "fa fa-lg fa-trash"}]]]])


(defn sign-in-form [user form-errors]
  [:form {:class "form-signin"
          :on-submit (fn [e] (do (sign-in user)
                                 (.preventDefault e)))}
   [:h2 "Please sign in"]
   [:div {:class "form-group"}
    (if form-errors [:div {:class "alert alert-danger" :role "alert"} (:error form-errors)])
    [:input {:class       "form-control"
             :type        "email"
             :placeholder "Email address"
             :value (:email user)
             :on-change   #(do (re-frame/dispatch-sync [:set-form-errors nil])
                               (re-frame/dispatch-sync [:set-user-login (assoc user :email (-> % .-target .-value))]))}]
    [:input {:class       "form-control"
             :type        "password"
             :placeholder "Password"
             :value (:password user)
             :on-change   #(do (re-frame/dispatch-sync [:set-form-errors nil])
                               (re-frame/dispatch-sync [:set-user-login (assoc user :password (-> % .-target .-value))]))}]]
   [:div
    [:p
     [:span "Or go to "]
     [:a {:href "#/sign-up"} "Sign up"]
     [:span " page"]]]
   [:input {:class "button btn btn-success"
            :type :submit
            :on-click (fn [e] (do (sign-in user)
                                  (.preventDefault e)))
            :value "Sign in"}]])


(defn sign-up-form [user form-errors]
  [:form {:class "form-signin"
          :on-submit (fn [e] (do
                               (if (and (= (:password user) (:confirm-password user)) (not-empty (:password user)))
                                 (sign-up user)
                                 (re-frame/dispatch [:set-form-errors {:error "Please enter a password"}]))
                               (.preventDefault e)))}
   [:h2 "Please sign in"]
   [:div {:class "form-group"}
    (if form-errors [:div {:class "alert alert-danger" :role "alert"} (:error form-errors)])
    [:input {:class       "form-control"
             :type        "email"
             :placeholder "Email address"
             :value (:email user)
             :on-change   #(do (re-frame/dispatch-sync [:set-form-errors nil])
                               (re-frame/dispatch-sync [:set-user-login (assoc user :email (-> % .-target .-value))]))}]
    [:input {:class       "form-control"
             :type        "password"
             :placeholder "Password"
             :value (:password user)
             :on-change   #(do (re-frame/dispatch-sync [:set-user-login (assoc user :password (-> % .-target .-value))])
                               (if (= (:confirm-password user) (-> % .-target .-value))
                                 (re-frame/dispatch-sync [:set-form-errors nil])
                                 (re-frame/dispatch-sync [:set-form-errors {:error "Passwords missmatch"}]))
                               )}]
    [:input {:class       "form-control"
             :type        "password"
             :placeholder "Confirm password"
             :value (:confirm-password user)
             :on-change   #(do (re-frame/dispatch-sync [:set-user-login (assoc user :confirm-password (-> % .-target .-value))])
                               (if (= (:password user) (-> % .-target .-value))
                                 (re-frame/dispatch-sync [:set-form-errors nil])
                                 (re-frame/dispatch-sync [:set-form-errors {:error "Passwords missmatch"}])))}]]
   [:div
    [:p
     [:span "Or go to "]
     [:a {:href "#/sign-in"} "Sign in"]
     [:span " page"]]]
   [:input {:class "button btn btn-success"
            :type :submit
            :on-click (fn [e] (do
                                (if (and (= (:password user) (:confirm-password user)) (not-empty (:password user)))
                                  (sign-up user)
                                  (re-frame/dispatch [:set-form-errors {:error "Please enter a password"}]))
                                (.preventDefault e)))
            :value "Sign up"}]])


(defn todo-lists [lists]
  [:ul {:class "list-group"}
   (for [list lists]
     ^{:key (:id list)}
     [:li {:class "list-group-item"}
      [:span {:class "fa fa-lg fa-list-ul"}]
      [:a {:href (str "#/lists/" (:id list))}
       (:title list)]
      [:span {:class    "button fa fa-lg fa-trash pull-right"
              :on-click (fn [] (do (nav! (str "/"))
                                   (delete-todo-list (:id list))))}]])])


(defn check-box [& {:keys [handler checked] :or {checked false}}]
  (let [state (atom checked)]
    [:span {:class (if @state "fa fa-check-square-o" "fa fa-square-o")
            :on-click (fn [] (do (handler)))}]))


(defn todos [todos display-completed]
  [:ul {:class "list-group"}
   (for [todo todos]
     (if (or (= (:completed todo) false) (= display-completed true))
       ^{:key (:id todo)}
       [:li {:class "list-group-item"}
        [:div {:class (str "priority-list-item priority-" (:priority todo))}
         [check-box :handler (fn []   (toggle-todo todo))
                    :checked (:completed todo)]]
        [:a {:href (str "#/lists/" (:todo_list_id todo) "/todos/" (:id todo))
             :class "todo-item"}
         (:title todo)]
        [:span {:class "button fa fa-lg fa-trash pull-right"
                :on-click (fn [] (do (nav! (str "/lists/" (:todo_list_id todo)))
                                     (delete-todo (:todo_list_id todo) (:id todo))))}]]))])


(defn toggle-completed-tasks [display-completed]
  [:div {:class "form-group"}
   (if (= display-completed true)
     [:input {:on-click #(re-frame/dispatch [:set-display-completed false])
              :type "button"
              :class "btn btn-primary center-block"
              :value "Hide completed"}]
     [:input {:on-click #(re-frame/dispatch [:set-display-completed true])
              :type "button"
              :class "btn btn-primary center-block"
              :value "Show completed"}])])