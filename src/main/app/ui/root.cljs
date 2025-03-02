(ns app.ui.root
  (:require
   [app.model.session :as session]
   [app.ui.pet-ui :as a-pet]
   [app.ui.game-cp :as game-cp]
   [clojure.string :as str]
   [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
   [com.fulcrologic.fulcro.dom.html-entities :as ent]
   [com.fulcrologic.fulcro.dom.events :as evt]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
   [com.fulcrologic.fulcro.ui-state-machines :as uism :refer [defstatemachine]]
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.fulcro-css.css :as css]
   [com.fulcrologic.fulcro.algorithms.form-state :as fs]
   [taoensso.timbre :as log]
   
   [app.mutations :as mymus]
   [com.fulcrologic.fulcro.data-fetch :as df]))

(defn field [{:keys [label valid? error-message] :as props}]
  (let [input-props (-> props (assoc :name label) (dissoc :label :valid? :error-message))]
    (div :.ui.field
      (dom/label {:htmlFor label} label)
      (dom/input input-props)
      (dom/div :.ui.error.message {:classes [(when valid? "hidden")]}
        error-message))))

(defsc SignupSuccess [this props]
  {:query         ['*]
   :initial-state {}
   :ident         (fn [] [:component/id :signup-success])
   :route-segment ["signup-success"]}
  (div
    (dom/h3 "Signup Complete!")
    (dom/p "You can now log in!")))

(defsc Signup [this {:account/keys [email password password-again] :as props}]
  {:query             [:account/email :account/password :account/password-again fs/form-config-join]
   :initial-state     (fn [_]
                        (fs/add-form-config Signup
                          {:account/email          ""
                           :account/password       ""
                           :account/password-again ""}))
   :form-fields       #{:account/email :account/password :account/password-again}
   :ident             (fn [] session/signup-ident)
   :route-segment     ["signup"]
   :componentDidMount (fn [this]
                        (comp/transact! this [(session/clear-signup-form)]))}
  (let [submit!  (fn [evt]
                   (when (or (identical? true evt) (evt/enter-key? evt))
                     (comp/transact! this [(session/signup! {:email email :password password})])
                     (log/info "Sign up")))
        checked? (fs/checked? props)]
    (div
      (dom/h3 "Signup")
      (div :.ui.form {:classes [(when checked? "error")]}
        (field {:label         "Email"
                :value         (or email "")
                :valid?        (session/valid-email? email)
                :error-message "Must be an email address"
                :autoComplete  "off"
                :onKeyDown     submit!
                :onChange      #(m/set-string! this :account/email :event %)})
        (field {:label         "Password"
                :type          "password"
                :value         (or password "")
                :valid?        (session/valid-password? password)
                :error-message "Password must be at least 8 characters."
                :onKeyDown     submit!
                :autoComplete  "off"
                :onChange      #(m/set-string! this :account/password :event %)})
        (field {:label         "Repeat Password" :type "password" :value (or password-again "")
                :autoComplete  "off"
                :valid?        (= password password-again)
                :error-message "Passwords do not match."
                :onChange      #(m/set-string! this :account/password-again :event %)})
        (dom/button :.ui.primary.button {:onClick #(submit! true)}
          "Sign Up")))))

(declare Session)

(defsc Login [this {:account/keys [email]
                    :ui/keys      [error open?] :as props}]
  {:query         [:ui/open? :ui/error :account/email
                   {[:component/id :session] (comp/get-query Session)}
                   [::uism/asm-id ::session/session]]
   :css           [[:.floating-menu {:position "absolute !important"
                                     :z-index  1000
                                     :width    "300px"
                                     :right    "0px"
                                     :top      "50px"}]]
   :initial-state {:account/email "" :ui/error ""}
   :ident         (fn [] [:component/id :login])}
  (let [current-state (uism/get-active-state this ::session/session)
        {current-user :account/name} (get props [:component/id :session])
        initial?      (= :initial current-state)
        loading?      (= :state/checking-session current-state)
        logged-in?    (= :state/logged-in current-state)
        {:keys [floating-menu]} (css/get-classnames Login)
        password      (or (comp/get-state this :password) "")] ; c.l. state for security
    (dom/div
      (when-not initial?
        (dom/div :.right.menu
          (if logged-in?
            (dom/button :.item            
              {:onClick #(uism/trigger! this ::session/session :event/logout)      
                    :tabIndex "-1"   ;;;;;;;;;;;;; ADDED BY MEEEEEE!!!!!!!!
               }
              (dom/span current-user) ent/nbsp "Log out")
            (dom/div :.item {:style   {:position "relative"}
                             :onClick #(uism/trigger! this ::session/session :event/toggle-modal)}
              "Login"
              (when open?
                (dom/div :.four.wide.ui.raised.teal.segment {:onClick (fn [e]
                                                                        ;; Stop bubbling (would trigger the menu toggle)
                                                                        (evt/stop-propagation! e))
                                                             :classes [floating-menu]}
                  (dom/h3 :.ui.header "Login")
                  (div :.ui.form {:classes [(when (seq error) "error")]}
                    (field {:label    "Email"
                            :value    email
                            :onChange #(m/set-string! this :account/email :event %)})
                    (field {:label    "Password"
                            :type     "password"
                            :value    password
                            :onChange #(comp/set-state! this {:password (evt/target-value %)})})
                    (div :.ui.error.message error)
                    (div :.ui.field
                      (dom/button :.ui.button
                        {:onClick (fn [] (uism/trigger! this ::session/session :event/login {:username email
                                                                                             :password password}))
                         :classes [(when loading? "loading")]} "Login"))
                    
                       ;;; link to signup-form, "signup"-url probably still active
                    #_(div :.ui.message
                      (dom/p "Don't have an account?")
                      (dom/a {:onClick (fn []
                                         (uism/trigger! this ::session/session :event/toggle-modal {})
                                         (dr/change-route this ["signup"]))}
                        "Please sign up!"))))))))))))

(def ui-login (comp/factory Login))

(defsc Main [this props]
  {:query         [:main/welcome-message]
   :initial-state {:main/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :main])
   ;:route-segment ["main"]
   }
  (div :.ui.container.segment
    (h3 "Hey, ")
    (p (str "diese App ist noch nicht öffentlich erreichbar. Falls du bereits einen Account hast, melde dich bitte an. :)")) 
    ))

(def ui-main (comp/factory Main))

(defsc Settings [this {:keys [:account/time-zone :account/real-name] :as props}]
  {:query         [:account/time-zone :account/real-name :account/crap]
   :ident         (fn [] [:component/id :settings])
   :route-segment ["settings"]
   :initial-state {}}
  (div :.ui.container.segment
    (h3 "Settings")
    (div "TODO")))

#_(defsc Aufgaben [this props]
  {:query         [:aufgaben/welcome-message]
   :initial-state {:aufgaben/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :aufgaben])
   :route-segment ["aufgaben"]}
  (div :.ui.container.segment
    (h3 "Your Pet")
    (p (str "it's quite healthy... for now"))))

#_(defsc Shop [this props]
  {:query         [:shop/welcome-message]
   :initial-state {:shop/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :shop])
   :route-segment ["shop"]}
  (div :.ui.container.segment
    (h3 "Your Pet")
    (p (str "it's quite healthy... for now"))))

#_(defsc Skilltree [this props]
  {:query         [:skilltree/welcome-message]
   :initial-state {:skilltree/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :skilltree])
   :route-segment ["skilltree"]}
  (div :.ui.container;.segment

    (h3 "Choose your Starter")
    (dom/button " Pet 1 ")
    (dom/button " Pet 2 ")
    (dom/button " Pet 3 ")
    (dom/img {:src "skilltree.svg"})))

(defsc Kurse [this props]
  {:query         [:kurse/welcome-message] 
   :initial-state {:kurse/welcome-message "Hi!"}
   :ident         (fn [] [:component/id :kurse])
   :route-segment ["kurse"]}
  (div

  (div ;:.ui.container.segment
    (dom/h1 "Kurseeee....") 
  )

  

  (div :.ui.container.segment
    (h3 "Above the Fold")
  (h3 "Gegenuberstellung")
  (h3 "Cycle of Doom")
  (h3 "Promise")

  (h3 "Aufgaben (20% + 10%)")
    (dom/ol 
      (dom/li "abc")
      (dom/li "exist")
      (dom/li "max")
      (dom/li "rotation")
    )
  (h3 "Video")
  (button "Dont care. Just show me Exercises.") (button "Book a Seat")
  )

  (div :.ui.container.segment
    (h3 "Modul 2 (part of 60%)")
    (dom/ol 
      (dom/li "dreiecke")
      (dom/li "kreis")
      (dom/li "aufloesen")
    )
    (button {:disabled true} "Dont care. Just show me Exercises.") (button {:disabled true} "Book a Seat")
    ;(p (button {:disabled true} "Modul 2"))
  )

  (div :.ui.container.segment
    (h3 "Modul 3 (covered 70% in total)")
    (dom/ol 
      (dom/li "FS")
      (dom/li "Prozent")
      (dom/li "Zeichnen")
      (dom/li "Schreiben")
    )
    (button {:disabled true} "Dont care. Just show me Exercises.") (button {:disabled true} "Book a Seat")
    ;(p (button {:disabled true} "Modul 3"))
  )

     
  ))

(dr/defrouter TopRouter [this props]
  {:router-targets [a-pet/Pet a-pet/Questsection a-pet/Shop a-pet/Skilltree Kurse Signup SignupSuccess Settings]})

(def ui-top-router (comp/factory TopRouter))

(defsc Session
  "Session representation. Used primarily for server queries. On-screen representation happens in Login component."
  [this {:keys [:session/valid? :account/name] :as props}]
  {:query         [:session/valid? :account/name]
   :ident         (fn [] [:component/id :session])
   :pre-merge     (fn [{:keys [data-tree]}]
                    (merge {:session/valid? false :account/name ""}
                      data-tree))
   :initial-state {:session/valid? false :account/name ""}})

(def ui-session (comp/factory Session))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmutation clicker [{:demo/keys [id]}]
  (action [{:keys [state]}]
          (swap! state update-in [:demo/id id :demo/counter] inc)))

(defsc FulcroDemo
  [this {:demo/keys [counter id]}]
  {:initial-state {:demo/counter 0 :demo/id :param/id}
   :ident        :demo/id ;(fn [] {:demo/id :param/id}); [:demoshit :countershit]
   
   :query         [:demo/counter :demo/id]}
  (dom/div {:style {:color "white" :zIndex "10" }} ;;still complaining???
    (str "Fulcro counter demo [" counter "]")
    (dom/button {:onClick #(comp/transact! this `[(clicker {:demo/id 22})])} "++++++") 
    (dom/button {:onClick #(df/load! this  (comp/get-ident this) ;[:demo/id 22]
                                     FulcroDemo ;{:target [:demoshit :countershit]}
    )} "load")
       
      (dom/svg {:width "200" :height "200" :xmlns "http://www.w3.org/2000/svg"}
       (dom/rect #js {;:onClick      #(df/load! this  [:game/id 1] ;(comp/get-ident this) ;[:demo/id 22]
                  ;a-pet/GameCP ;{:target [:game/id]}
                  ;)         ;#(comp/transact! this `[(mymus/spitti {:state "yo"})])
                      :width "25" :height "25" :style #js {:fill        "rgb(200,200,200)"
                                                    :strokeWidth "2"
                                                    :stroke      "black"}
                                                 })
       
       )))

(def cnt-demo (comp/factory FulcroDemo {:keyfn :demo/id}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defsc TopChrome [this {:root/keys [router current-session login demoshit game]}]
  {:query         [{:root/router (comp/get-query TopRouter)}
                   {:root/current-session (comp/get-query Session)}
                   [::uism/asm-id ::TopRouter]
                   {:root/login (comp/get-query Login)}
                   {:root/demoshit (comp/get-query FulcroDemo)}
                   {:root/game (comp/get-query game-cp/GameCP)}]
   :ident         (fn [] [:component/id :top-chrome])
   :initial-state {:root/router          {}
                   :root/login           {}
                   :root/current-session {}
                   :root/demoshit {:id 22}
                   :root/game {:id 1}}}
  (let [current-tab (some-> (dr/current-route this this) first keyword)
        logged-in? (:session/valid? current-session)
        logged-out? (not logged-in?)]
    (div :.ui.container.app_ui_root_Root__underwater {:style {:minWidth "100vw" :minHeight "100vh"} }
     (div :.ui.container
         (div :.ui.secondary.pointing.menu
              (if logged-in? 
               (dom/a :.item {:classes [(when (= :pet current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["pet"]))} "Pet")
              )
              (if logged-in?
               (dom/a :.item {:classes [(when (= :quests current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["quests"]))} "Aufgaben")
              )
              (if logged-in?  
               (dom/a :.item {:classes [(when (= :shop current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["shop"]))} "Shop")
              )
              (if logged-in?  
               (dom/a :.item {:classes [(when (= :skilltree current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["skilltree"]))} "Skilltree")
              )
              (if logged-in?  
               (dom/a :.item {:classes [(when (= :kurse current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["kurse"]))} "Kurse")
              )
              (if logged-in?  
               (dom/a :.item {:classes [(when (= :settings current-tab) "active")]
                             :onClick (fn [] (dr/change-route this ["settings"]))} "Settings")
              )
              #_(dom/a :.item {:classes [(when (= :main current-tab) "active")]
                               :onClick (fn [] (dr/change-route this ["main"]))} "Your Pet")
              (div :.right.menu
                   (ui-login login)))
         (div :.ui.grid
              (div :.ui.row 
                 ;{:id "container-for-tower-defense"}
                   (if logged-in? (ui-top-router router) 
                                  (ui-main))
                   )
              (div {:style {:minWidth "100%" :minHeight "300px"}})
              (div :.ui.grid 
                 
                   ;(p "part of page layout") 
                   ;(p (str (:session/valid? current-session) (:account/name current-session)))
                   ;(cnt-demo demoshit )
                   ;(if logged-in? (str (:game/id game)))
                   #_(if logged-in? (dom/button {:onClick #(df/load! this [:game-state (:account/name current-session)] ;[:demo/id 22]
                                                    game-cp/GameCP ;{:target [:component/id :game]}
                                                    )} "load game"))
                   ;(p (str (some-> (dr/current-route this this) first keyword)))
                   ))))))


(def ui-top-chrome (comp/factory TopChrome))

(defsc Root [this {:root/keys [top-chrome]}]
  {:query         [{:root/top-chrome (comp/get-query TopChrome)}]
   :initial-state {:root/top-chrome {}}
   :css [[:.underwater {;:background "rgb(26,155,227)"
                        :background "linear-gradient(160deg, rgba(26,155,227,1) 0%, rgba(7,50,190,1) 43%, rgba(0,6,66,1) 100%)"}]
         [:.transparentbackground {:background-color "transparent"}]]}
  (ui-top-chrome top-chrome))
