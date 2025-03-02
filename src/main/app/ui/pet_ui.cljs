(ns app.ui.pet-ui
  (:require
    ;[app.ui.components :as a-comp]

   [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h2 h3 button b]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]

   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   [com.fulcrologic.fulcro-css.css :as css]
   [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]

   [app.ui.aufgaben :as aufg]
   [app.ui.towerdefense :as tower]
   [app.ui.game-cp :as game :refer [card-sheet stages stage-by-xp linearize-cards categorize-cards GameCP pet-level]]

   ;["react-spring" :refer [useSpring animated]]
   ;[com.fulcrologic.fulcro.algorithms.react-interop :as interop]


   ))


(defn svg123 []
  (let [w 300 h 100]
      (dom/svg {:width (str w) :height (str h) :xmlns "http://www.w3.org/2000/svg"}
       (dom/rect #js {:width (str w) :height (str h) :style #js {:fill        "rgb(200,200,200)"
                                                    :strokeWidth 2
                                                    :stroke      "black"}})
       (dom/text  {:textAnchor "middle" :x (str (/ w 2)) :y (str (/ h 2))} (str w "x" h)))
       ))






(defn popup-msg [{:keys [msg]}]
  (js/alert msg))






  (defn card-svg [{:keys [txt det]}]
  
   (dom/svg {:width "100" :height "130"}
              (dom/defs 
               (dom/clipPath {:id "card-outline"}
                 (dom/rect {:x "10" :y "10" :rx "5px" :ry "5px" :width "80" :height "110" })
                )
               (dom/linearGradient {:id "cardbaseGrad" :x1 "0" :x2 "0" :y1 "0" :y2 "1"}
                 (dom/stop {:offset "0%" :stopColor "rgba(180,200,255,1)"})
                 (dom/stop {:offset "100%" :stopColor "rgba(180,250,220,1)"})) 
               (dom/linearGradient {:id "cardsecondGrad" :x1 "0" :x2 "0" :y1 "0" :y2 "1"}
                 (dom/stop {:offset "20%" :stopColor "rgba(100,250,150,1)"})
                 (dom/stop {:offset "100%" :stopColor "rgba(0,210,50,1)"})) 
              )
                (dom/rect {:x "10" :y "10" :rx "5px" :ry "5px" :width "80" :height "110" :fill "url(#cardbaseGrad)";"linear-gradient(160deg, rgba(26,155,227,1) 0%, rgba(7,50,190,1) 43%, rgba(0,6,66,1) 100%)" ;"rgba(200,200,220,0.9)" 
                :stroke "none" :strokeWidth "1px"}
                  )
                (dom/ellipse {:cx "50" :cy "-15" :rx "65" :ry "63" :fill "url(#cardsecondGrad)" :clipPath "url(#card-outline)"})
               
               (dom/circle {:cx "50" :cy "55" :r "20" :fill "orange"})
               
               (dom/g {:transform "translate(30,90) scale(0.8)"} (dom/text  txt))
               (if det (dom/g {:transform "translate(30,95) scale(0.4)"} (dom/text  det)))

                (dom/path {:d "M10,102L 40,102L 30,110L 10,110L 10,102Z" :fill "rgba(0,0,0,0.4)" :stroke "none" :strokeWidth "2px"})
                (dom/path {:d "M45,102L 62,102L 52,110L 35,110L 45,102Z" :fill "rgba(0,0,0,0.3)" :stroke "none" :strokeWidth "2px"})
                (dom/path {:d "M67,102L 84,102L 74,110L 57,110L 67,102Z" :fill "rgba(0,0,0,0.2)" :stroke "none" :strokeWidth "2px"})
                
                )
          
  )

  ;;;;; Display shop stuff
  ;; TODO: detailansicht wenn man auf karte klickt
(defn render-card [this {:keys [modal cash tracker fun card-action]}]
     (fn [k]
      (let [card (k card-sheet)
           price (:price card)
           up-price (:up-price card)
           name (:name card)
           det (:det card)
           color (:color card)
           aufgabe? (contains? #{:a :b} (:cat card))
           zoom-card #(js/console.log "zoom card")
           
           click-fun (case fun :buy #(if (>= cash price) (comp/transact! this `[(shop-payment ~{:id 1 :price price}) (activate-card ~{:id 1 :card card :aufgaben-tracker tracker})]) (popup-msg {:msg "you are broke"}))
                               :unlock #(if (>= cash up-price) (comp/transact! this `[(shop-payment ~{:id 1 :price up-price}) (unlock-card ~{:id 1 :k k})])
                                                            (popup-msg {:msg "you are broke"}))
                               :deck (if (get modal :card false) #(do (comp/transact! this `[(replace-deck-card ~{:replace (:k card) :add (:card modal)})])
                                                                      (comp/transact! this `[(toggle-shop-modal ~{:modal :cards :card nil})])  
                                                                    )
                                                      zoom-card)    
                               :put-in-deck #(comp/transact! this `[(toggle-shop-modal ~{:modal :cards :card (:k card)})])                                                
                               :detail #(comp/transact! this `[(toggle-shop-modal ~{:detailed true :card (:k card) :card-action card-action})])  
                               (fn [x])
                               )] 
      
      (div {:onClick click-fun :style (if (= fun :unlock) {:opacity "40%"} {})} 
         (if (= fun :detailed) (dom/svg {:width "500" :height "650"} (dom/g {:transform "scale(5,5)"} (card-svg {:txt name :det det})))   
                             (card-svg {:txt name})))

      ;REMOVE
      #_(div {:style {:maxWidth "6em" :minHeight "9em" :borderStyle "outset" :padding "1em" 
                   :display "flex" :flexDirection "column" :justifyContent "space-between"
                   :margin "0 0.5em" 
                   :opacity "75%"
                   :borderRadius "0.4em"
                   :backgroundColor color
                   }
                   :onClick click-fun
           }
          (dom/h4 name)
          (p (str " " aufgabe?) )
          (p "$ " price)
          )   
          
          
          ) )     
  )


















(defn display-map [this xp stage]
  (let [full-circle (fn [x y] (dom/circle {:cx (str x) :cy (str y) :r  "20" :fill "white"}))
        empty-circle (fn [x y] (dom/circle {:cx (str x) :cy (str y) :r  "20" :fill "none" :stroke "white" :strokeWidth "6px"}))
        focus-circle (fn [x y] (dom/g (full-circle x y) (dom/circle {:cx (str x) :cy (str y) :r  "30" :fill "none" :stroke "white" :strokeWidth "5px"})))
        line-style {:fill "none" :stroke "white" :strokeWidth "7px" :strokeMiterlimit "1.5" :strokeDasharray "16,20,0,0"}
        bezier-calc (fn [x1 y1 x2 y2]        (str "M" x1 "," y1 "C" 
                                        (+ x1 (/ (- x2 x1) 2)) "," y1  " "
                                        (+ x1 (/ (- x2 x1) 2)) "," y2 " "
                                                            x2 "," y2))
        prog-bar (fn [cur max unit] (dom/g 
                                      (dom/rect {:x "50" :y "50" #_#_:rx "1px" :ry "10px" :width "300" :height "20" :fill "none" :stroke "white" :strokeWidth "2px"})
                                      (dom/rect {:x "50" :y "50" #_#_:rx "1px" :ry "10px" :width (str (min (int (* 300 (/ cur max))) (* 300 max))) :height "20" :fill "white" :stroke "none" })
                                      (dom/text {:x "360" :y "65" :fill "white"} (str cur " / " max " " unit))    
                                          ))
        max-stage (stage-by-xp xp)
          ]
         (dom/svg {:width "950" :height "500" :xmlns "http://www.w3.org/2000/svg"}
                (dom/rect {:x "25" :y "30" :rx "5px" :ry "5px" :width "900" :height "450" :fill "none" :stroke "white" :strokeWidth "1px"})
                (prog-bar xp (:xp (get stages max-stage)) "XP")

                (dom/path {:d (bezier-calc 100 250 280 400) :style line-style})   ;"M100,250 C 200,250 200,400 300,400"
                (if (= stage 1) (focus-circle 100 250) (dom/g {:onClick #(comp/transact! this `[(switch-stage {:stage 1})])} (full-circle 100 250)))
                (dom/path {:d (bezier-calc 330 400 475 150) :style line-style})   ;"M100,250 C 200,250 200,400 300,400"
                (cond (= stage 2) (focus-circle 300 400) 
                      (< max-stage 2) (empty-circle 300 400)
                      (>= max-stage 2) (dom/g {:onClick #(comp/transact! this `[(switch-stage {:stage 2})])} (full-circle 300 400)))
                (dom/path {:d (bezier-calc 530 140 680 300) :style line-style})   ;"M100,250 C 200,250 200,400 300,400"
                (cond (= stage 3) (focus-circle 500 150) 
                      (< max-stage 3) (empty-circle 500 150)
                      (>= max-stage 3) (dom/g {:onClick #(comp/transact! this `[(switch-stage {:stage 3})])} (full-circle 500 150)))
                (cond (= stage 4) (focus-circle 700 300) 
                      (< max-stage 4) (empty-circle 700 300)
                      (>= max-stage 4) (dom/g {:onClick #(comp/transact! this `[(switch-stage {:stage 4})])} (full-circle 700 300)))   
                

  )))

(defsc Pet [this {:pet/keys [tower cards temp-modal game-state stage lvl-up-modal show-map] :as props}]
  {:query         [{:pet/tower (comp/get-query tower/TowerDefenseRenderer)} :pet/cards :pet/temp-modal
                   {:pet/game-state (comp/get-query GameCP)} :pet/stage :pet/lvl-up-modal :pet/show-map]
   :initial-state {:pet/tower {:id 1 :unit 40 ;;:sx 30 :sy 15 #_#_:grid (vec (for [y (range 15)] (vec (repeat 30 :empty))))
                              }
                   :pet/cards {:red {:name "red plant" :color "#F55" :action :red :count 30}
                               :blue {:name "blue plant" :color "#55F" :action :blue :count 0}
                               :green {:name "green plant" :color "#5F5" :action :green :count 0}
                               :bait {:name "bait" :color "#5F5" :action :bait :count 5}} 
                   :pet/temp-modal false
                   :pet/show-map true
                   :pet/game-state {:id 1} 
                   :pet/lvl-up-modal false
                   :pet/stage (stage-by-xp (:game-cp/xp :param/game-state))}
   :ident         (fn [] [:component/id :pet])
   :route-segment ["pet"]}
  (div :.ui.container;.segment 
      

          
       ;;; TODO maybe not insta-display!
       (if (> (get-in game-state [:game-cp/pet :ups] 0) 0) 
       (button {:onClick #(comp/transact! this `[(toggle-lvl-up-modal {:target true})])} "get stuff"))
       (if lvl-up-modal 
          (div {:style {:backgroundColor "white" :position "absolute" :zIndex "3" :minHeight "50vh" :width "100%"}}
            (button {:onClick #(comp/transact! this `[(toggle-lvl-up-modal {})])} "close")
            
            (button {:onClick #(comp/transact! this `[(game/gain-pet-stats {:game-id 1 :stat :max-hp :amount 10 }) (game/gain-pet-stats {:game-id 1 :stat :hp :amount 10 }) (toggle-lvl-up-modal {})])} "+ 10 HP")
            (button {:onClick #(comp/transact! this `[(game/gain-pet-stats {:game-id 1 :stat :pw :amount 2  }) (toggle-lvl-up-modal {})])} "+ 2 PWR")
            (button {:onClick #(comp/transact! this `[(game/gain-pet-stats {:game-id 1 :stat :int :amount 5 }) (toggle-lvl-up-modal {})])} "+ 5 INT")
            ))



       (if show-map 
        ;;KARTE
        (display-map this (get game-state :game-cp/xp 0) stage)
        
       )

  (if (= stage 1) 
   (h3 "dating-app-like swiping??? ")
   )


  ;(if show-map

  
      (if (and (= stage 2)) 
         (if (get-in game-state [:game-cp/pet :hp] false)
           (div 
             (h3 (get-in game-state [:game-cp/pet :name] "unnamed"))
             (dom/img {:src "pet1.png"})
             (h3 "Level: " (game/pet-level :a (get-in game-state [:game-cp/pet :xp] 0)))
             (p "Health: " (get-in game-state [:game-cp/pet :hp] 0) " / " (get-in game-state [:game-cp/pet :max-hp] 0))
             (p "Strength: " (get-in game-state [:game-cp/pet :pw] 0))
             (p "Intelligence: " (get-in game-state [:game-cp/pet :int] 0))
             (tower/ui-towergame tower)
             )
             

           (div 
            (h3 "Choose your Starter")
            (dom/img {:onClick #(comp/transact! this `[(get-pet {:type :a :name "lexi"})]) :src "pet1.png"}))         
         )
        )

      (if (= stage 3) (dom/img {:src "underwat.png"}))

      


      ;; no map: empty buffer. only there because display-card is at wrong place
      #_(div {:style {:minHeight "630px"}})

  ;)
        ;draw pet in TD
        #_(if (and (>= stage 2) temp-modal (get-in game-state [:game-cp/pet :name] false)) (dom/img {:src "pet1.png" :style {:position "fixed" :top "290px" :left "460px"}}) )



       

       

       (button {:onClick #(comp/transact! this `[(toggle-map {})])} "Karte")

       (button {:onClick #(comp/transact! this `[(gain-xp {:amount 70})])} "get xp")
       (button {:onClick #(comp/transact! this `[(gain-xp {:amount -70})])} "lose xp") 





      ;; Plant Cards

       (let [display-item (fn [{:keys [name color action count]}]
                  (if (pos? count)
                    (div {:style {:maxWidth "6em" :minHeight "9em" :borderStyle "outset" :padding "1em" 
                        :display "flex" :flexDirection "column" :justifyContent "space-between"
                        :margin "0 0.5em" 
                        :opacity "75%"
                        :borderRadius "0.4em"
                        :backgroundColor color}
                        :onClick #(comp/transact! this `[(tower/change-action ~{:tower-render/id 1 :new-action action})
                                                         (remove-plant-card ~{:td-action action})])
                        }
                    (dom/h4 name)
                    (p " ")
                    (p (str "x" count))
                    ;(p "$ " price)
                  ))         
                  )
              ]
              (div {:style {:display "flex" :justifyContent "center" }}
                (map display-item (vals cards)))

        )
              
       
     ))


(defsc Skilltree [this {:skilltree/keys [game-state] :as props}]
  {:query         [{:skilltree/game-state (comp/get-query GameCP)}]
   :initial-state {:skilltree/game-state {:id 1}}
   :ident         (fn [] [:component/id :skilltree])
   :route-segment ["skilltree"]}
  (div :.ui.container;.segment

    (if-let [name (get-in game-state [:game-cp/pet :name] false)]
      (div (h3 name)
           (h3 (str "XP: " (get-in game-state [:game-cp/pet :xp] 0))))
    )

    (h3 "Level: " (game/pet-level :a (get-in game-state [:game-cp/pet :xp] 0)))
    (p "Health: " (get-in game-state [:game-cp/pet :max-hp] 0) )
    (p "Strength: " (get-in game-state [:game-cp/pet :pw] 0))
    (p "Intelligence: " (get-in game-state [:game-cp/pet :int] 0))
    

    ;;;; SKILLTREE
    (let [size 10
          full-circle (fn [x y] (dom/circle {:cx (str x) :cy (str y) :r  "25" :fill "white"}))
          empty-circle (fn [x y] (dom/circle {:cx (str x) :cy (str y) :r  (str (* size 2)) :fill "none" :stroke "white" :strokeWidth (str (* size 0.4) "px")}))
          triple-circle (fn [x y] (dom/g 
                            (dom/circle {:cursor "pointer" :cx (str x) :cy (str y) :r  (str (* size 2)) :fill "rgba(0,0,0,0)" :stroke "white" :strokeWidth (str (* size 0.4) "px")})
                            (dom/circle {:cx (str x) :cy (str y) :r  (str (* size 2.6)) :fill "none" :stroke "rgba(255,255,255,0.8)" :strokeWidth (str (* size 0.3) "px")})
                            (dom/circle {:cx (str x) :cy (str y) :r  (str (* size 3.1)) :fill "none" :stroke "rgba(255,255,255,0.5)" :strokeWidth (str (* size 0.2) "px")})))
          line-style {:fill "none" :stroke "white" :strokeWidth "4px" }
          path-calc (fn [x1 y1 x2 y2] (str "M" x1 "," y1 " L" x2 "," y2 "Z"))
          prog-bar (fn [cur max unit] (dom/g 
                                      (dom/rect {:x "50" :y "50" #_#_:rx "1px" :ry "10px" :width "300" :height "20" :fill "none" :stroke "white" :strokeWidth "2px"})
                                      (dom/rect {:x "50" :y "50" #_#_:rx "1px" :ry "10px" :width (str (min (int (* 300 (/ cur max))) (* 300 max))) :height "20" :fill "white" :stroke "none" })
                                      (dom/text {:x "360" :y "65" :fill "white"} (str cur " / " max " " unit))    
                                          ))
          xp-target (get-in game-state [:game-cp/pet :level-curve (dec (get-in game-state [:game-cp/pet :lvl] 1))] 1000)
          current-xp (get-in game-state [:game-cp/pet :xp] 0)
          ;_ (js/console.log (str (get-in game-state [:game-cp/pet :level-curve 2])))
          ]
      (dom/svg {:width "950" :height "800" :xmlns "http://www.w3.org/2000/svg"}
      
          (prog-bar (min xp-target current-xp) xp-target "XP")
          (dom/text {:x "50" :y "100" :fill "white"} "Skillpoints: " (get-in game-state [:game-cp/pet :sp] 0))
          
          (dom/path {:d "M100,750 L 700, 700 L 50, 650 L 750, 600 L 100,550 L 700,500 L 150,450 L 650,400 Z" :style {:fill "none" :stroke "black" :strokeWidth "90px" :strokeLinejoin "round"}})
          
          (full-circle 300 750)

          (dom/path {:d (path-calc 300 750 300 710) :style line-style})
          (dom/path {:d (path-calc 300 710 350 690) :style line-style})
          (dom/path {:d (path-calc 300 710 250 690) :style line-style})
          (dom/path {:d (path-calc 250 690 250 670) :style line-style})
          (dom/path {:d (path-calc 350 690 350 670) :style line-style})
          (dom/path {:d (path-calc 335 630 315 605) :style line-style})
          (dom/path {:d (path-calc 265 630 285 605) :style line-style})
          (dom/path {:d (path-calc 300 560 300 480) :style line-style})  
          (dom/path {:d (path-calc 320 580 350 560) :style line-style})  
          (dom/path {:d (path-calc 280 580 250 560) :style line-style})  
          (dom/path {:d (path-calc 350 560 350 530) :style line-style})  
          (dom/path {:d (path-calc 250 560 250 520) :style line-style})  
          (triple-circle 250 650)
          (triple-circle 350 650)
          ;(full-circle 450 300)
          (triple-circle 300 580)
          (empty-circle 300 450)
          (empty-circle 250 490)
          (empty-circle 350 500)
      
      
      ))
    
    
    (dom/img {:src "skilltree.svg"})))

























(defsc AufgabenIcon [this {:aufgabenicon/keys [id aufgabe active] :as props}]
  {:query [:aufgabenicon/id :aufgabenicon/active {:aufgabenicon/aufgabe (comp/get-query aufg/Aufgabe)}]
   :ident :aufgabenicon/id
   :initial-state {:aufgabenicon/id :param/id 
                   :aufgabenicon/active false
                   :aufgabenicon/aufgabe :param/aufgabe}
   }
  (let [complete? ((:aufgabe/state aufgabe) :complete)
        activecolor "rgb(140,180,255)"
        inactivecolor "rgb(220,240,255)"
        completecolor "rgb(150,255,190)"
        buttoncolor (if complete? completecolor (if (= active id) activecolor inactivecolor))
        ]
    (button {:onClick #(comp/transact! this `[(change-active-quest-task ~{:quest/active id})])
           :style {:backgroundColor buttoncolor :padding "0.5em 1em" :margin "1em 2px"}}
              (str id)))
  )
(def ui-aufg-icon (comp/factory AufgabenIcon {:keyfn :aufgabenicon/id }))

(defn render-quest [aufgabenliste active this]
  (div 
      
   ;(div {:style {:minHeight "20px"}})
   (for [x (range 1 (+ (count aufgabenliste) 1) )]
      (ui-aufg-icon {:aufgabenicon/id x :aufgabenicon/aufgabe (nth aufgabenliste (- x 1)) :aufgabenicon/active active})
     
     #_(button {:onClick #(comp/transact! this `[(change-active-quest-task ~{:quest/active x})])}
              (str x))
     )
   ;(div {:style {:minHeight "30px"}})
   
   
   (if active 
     (aufg/ui-aufgabe (nth aufgabenliste (- (min (count aufgabenliste) active) 1))))
   ))




(defsc Quest [this {:quest/keys [items aufgabenliste typ active] :as props}]
  {:query         [:quest/items :quest/typ :quest/active {:quest/aufgabenliste (comp/get-query aufg/Aufgabe)}
                    ]
   :initial-state {:quest/items ["Aufgabe 1: ..." "Aufgabe 2: ..." "Aufgabe 3: ..."] :quest/typ :bland :quest/active false 
                   :quest/aufgabenliste [];[{:id 1 :typ :typP1} {:id 2 :typ :typP1} {:id 3 :typ :typP1}] 
                   }
   :ident         (fn [] [:component/id :quest])
   :route-segment ["quest"]}
  (div :.ui.container;.segment
    #_(h3 "Quest: FancyName")
    
    (aufg/render-aufgabeP2 1 2 3 4 5)
    (aufg/render-aufgabeP3 2 2 3 4 5)
    (aufg/render-aufgabeP4 3 2 3 4 5)
    (aufg/render-dreieck1 4 2 3 4 5)
    (aufg/draw-triangle 100 100 200 240 500 260)
    (aufg/draw-triangle 200 100 200 250 400 250)
       
       (render-quest aufgabenliste active this)


    (button {:onClick (fn [] (do   ;;;; VERY IMPORTANT: RESET ACTIVE
                                 (comp/transact! this `[(change-active-quest-task ~{:quest/active false})])
                                 (dr/change-route this ["questlist"])
                                 ))}
                                 "Aufgeben")   
  
  
  ))

(defsc Questboard [this {:questboard/keys [items] :as props}]
  {:query         [:questboard/items]
   :initial-state {:questboard/items []}
   :ident         (fn [] [:component/id :questboard])
   :route-segment ["questlist"]}
  (div :.ui.container;.segment
    #_(h3 "Available Quests")
    #_(p (str "choose wisely"))

      #_(div {:style {:display "flex" :justifyContent "center"}}
            (map display-quest-icon items))

      (let [;quest (first items)
            start-quest (fn [aufg] (fn [] (do (comp/transact! this `[(generate-quest ~{:aufgabenliste aufg})])
                                              (dr/change-route this ["quest"])
                                    )))
            display-quest-icon (fn [{:keys [name price color aufg]}]
                        (div {:style {:maxWidth "6em" :minHeight "9em" :borderStyle "outset" :padding "1em" 
                              :display "flex" :flexDirection "column" :justifyContent "space-between"
                              :margin "0 0.5em" 
                              :opacity "75%"
                              :borderRadius "0.4em"
                              :backgroundColor color}
                              :onClick (start-quest aufg)}
                          (dom/h4 name)
                          (p " ")
                          (p "$ " price)
                          ))
            
            ]
            (div {:style {:display "flex" :justifyContent "center" :flexWrap "wrap"}}
            (map display-quest-icon items)
            (if (empty? items) (dom/img {:src "https://i2.pngimg.me/thumb/f/720/freesvgorg73286.jpg" :width "200px"}))
            )
            
       )
    
    
    ;(for [x items]
      #_(p (dom/a {:onClick (fn [] (dr/change-route this ["quest"]))}    "quest 1")) ;x))
    ;    )
    
       ;(h3 {:style {:textAlign "center" :color "white"}} "Verfugbare Aufgaben")
       #_(div {:style {:display "flex" :justifyContent "center"}}
            (map display-item [{:name "bundle 2" :available true :price 7 :color "#DD3"}
                               {:name "bundle 3" :available true :price 7 :color "#9DE"}]))
      ;(str items)
       
       ;(dom/img {:src "quests.svg"})


    #_(h3 "Dojo / Skilldeck")
    
  ))

(dr/defrouter QuestRouter [this props]
  {:router-targets [Questboard Quest]})

(def ui-quest-router (comp/factory QuestRouter))

(defsc Questsection [this {:questsection/keys [router]}]
  {:query         [{:questsection/router (comp/get-query QuestRouter)}]
   :initial-state {:questsection/router {}}
   :ident         (fn [] [:component/id :questsection])
   :route-segment ["quests"]}
  (div 
    (ui-quest-router router)
     
     #_(p (str (dr/current-route this this) #_(some-> (dr/current-route this this) first keyword
                     )))))























#_(def ui-spring (interop/react-factory animated.div))




(defsc Shop [this {:shop/keys [inventory game-state modal deck temp-stats] :as props}]
  {:query         [:shop/inventory {:shop/game-state (comp/get-query GameCP)} :shop/modal :shop/deck :shop/temp-stats]
   :initial-state {:shop/inventory {:a #{:a1}}
                   :shop/deck (list :a1 :a2 :a3 :a4 :a5 :a6)
                   :shop/modal {}                
                   :shop/game-state {:id 1}
                   :shop/temp-stats {:discount 1
                   :bonus 1 :single-counter 0}
                   }
   :ident         (fn [] [:component/id :shop])
   :route-segment ["shop"]}
  (let [cards-in-deck {:a (set (for [a (filter (fn [n] (= :a (:cat (n card-sheet)))) deck)] a)) 
                       :s (set (for [s (filter (fn [n] (= :s (:cat (n card-sheet)))) deck)] s)) }
        available-cards (:game-cp/cards game-state)    ;TODO: replace with DECK !!!! -- actually: no
        cash (:game-cp/cash game-state)
        tracker (:game-cp/aufgaben-id-tracker game-state)
        unlockable (:game-cp/unlockable game-state)
        discount (get temp-stats :discount 1)
        

        display-in-shop (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker (:game-cp/aufgaben-id-tracker game-state) :fun :buy })
        display-unlockable (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :unlock} )
        display-unlocked (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :put-in-deck}) 

        display-deck (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :deck})
        display-detailed-card (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :detailed})
        display-detail (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :detail})
        display-hand-card (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :detail :card-action :play})
        
        display-swipe-card (render-card this {:settings (:game-cp/settings game-state) :modal modal :cash cash :tracker false :fun :detailed})
        
        ]
    (div (div :.ui.container.segment
            (h2 "Shop")
            (p (str "Aktueller Kontostand: $" cash))



            (p " -------------------- "))


      (h3 {:style {:textAlign "center" :color "white"}} "SWIPE")
      (p {:style {:textAlign "center" :color "white"}} "75 XP ---- $ 57 --- Eco 2 - Speed 4 - Luck 2 - Team 3")
      (display-swipe-card :t1) 
      (button "Discard for $1")
      (button "buy for $8")


      ;; aktuelle Hand
       (h3 {:style {:textAlign "center" :color "white"}} "Your Cards")
       (div {:style {:display "flex" :justifyContent "center"}}
            (if (and (empty? (:a inventory)) (empty? (:s inventory)))  ;;;MIGHT BITE ME IN THE ASS
                  (dom/img {:src "https://i2.pngimg.me/thumb/f/720/freesvgorg73286.jpg" :width "200px"})
            )
            ;; If settings -> action
            ;(map display-in-shop (:a inventory))
            
            ;; If settings -> zoom
            (map display-hand-card (:a inventory))
            (map display-hand-card (:s inventory))
            
            
       )

       ;; Modal for playing a card
       (if (:detailed modal)
           (let [card-key (:card modal)
                 price (int (* discount (:price (card-key card-sheet))))
                 
                 play-card #(if (>= cash price) 
                                (comp/transact! this `[(shop-payment  ~{:id 1 :price price}) 
                                                       (activate-card ~{:id 1 :card (card-key card-sheet) :aufgaben-tracker tracker :available-cards cards-in-deck})
                                                       (toggle-shop-modal {:detailed false})]) 
                                (popup-msg {:msg "you are broke"}))]
            (div {:style {:position "fixed" :top "10vh" :left "10vw"}} 
             (display-detailed-card card-key #_(:card modal))  
             (button {:onClick play-card} (str (name (:card-action modal)) " for $" price #_(:price ((:card modal) card-sheet)) ))
            (if (= :byprice (:draw (card-key card-sheet)))
              (div 
                (button {:onClick #(if (>= cash (* 2 price)) 
                                (comp/transact! this `[(shop-payment  ~{:id 1 :price (* 2 price)}) 
                                                       (activate-card ~{:id 1 :card (card-key card-sheet) :aufgaben-tracker tracker :available-cards cards-in-deck :draw 2})
                                                       (toggle-shop-modal {:detailed false})]) 
                                (popup-msg {:msg "you are broke"}))
                         } (str (name (:card-action modal)) " for $" (* 2 price)  ))
                (button {:onClick #(if (>= cash (* 2 price)) 
                                (comp/transact! this `[(shop-payment  ~{:id 1 :price (* 3 price)}) 
                                                       (activate-card ~{:id 1 :card (card-key card-sheet) :aufgaben-tracker tracker :available-cards cards-in-deck :draw 3})
                                                       (toggle-shop-modal {:detailed false})]) 
                                (popup-msg {:msg "you are broke"}))
                         } (str (name (:card-action modal)) " for $" (* 3 price)  ))
                 ))
            (button {:onClick #(comp/transact! this `[(toggle-shop-modal {:detailed false})])} "Close")
            (if (:card2 modal)(display-detailed-card (:card2 modal)) ) 
            
            ))
        )


                 

      (button {:onClick #(comp/transact! this `[(toggle-shop-modal {:modal :deck})])} "Your Deck")

      ;; Modal for Deck-management
      
      
      (if (:modal modal) (div {:style {:backgroundColor "rgba(50,50,50,0.2)"}}

      
      (h3 {:style {:textAlign "center" :color "white"}} "Your Deck")
      (div {:style {:display "flex" :justifyContent "center"}}
            ;(div {:style {:minHeight "110px" :minWidth "80px" :borderStyle "solid" :borderColor "white" :borderStrength "3px" :borderRadius "5px" :margin "10px 10px"}})
            ;(div {:style {:minHeight "110px" :minWidth "80px" :borderStyle "solid" :borderColor "white" :borderStrength "3px" :borderRadius "5px" :margin "10px 10px"}})
            ;(div {:style {:minHeight "110px" :minWidth "80px" :borderStyle "solid" :borderColor "white" :borderStrength "3px" :borderRadius "5px" :margin "10px 10px"}})
       
            (map display-deck deck)
       
       )

      (button {:onClick #(comp/transact! this `[(toggle-shop-modal {:modal :cards})])} "Edit Deck")
      

      (button {:onClick #(comp/transact! this `[(toggle-shop-modal {:modal false})])} "Close")
      

       ;;;;; Zusammenfassen:    (deine karten --vs-- kaufbar --vs-- transparenz / schloss)
      (if (= (:modal modal) :cards) 
       (div
       (h3 {:style {:textAlign "center" :color "white"}} "Your Cards")
      
       (div {:style {:display "flex" :justifyContent "center" :flexWrap "wrap"}}
            (map display-unlocked (:a available-cards))
            ;(map display-unlocked (:b available-cards))
            (map display-unlocked (:p available-cards))
            (map display-unlocked (:s available-cards))

       )
       (div {:style {:display "flex" :justifyContent "center" :flexWrap "wrap"}}
            (map display-unlockable (:p unlockable))
            (map display-unlockable (:a unlockable))
            (map display-unlockable (:s unlockable))

       )
      ))




       ))   ;deck-modal end








       #_(h3 {:style {:textAlign "center" :color "white"}} "Shop-Cards")
       #_(div {:style {:display "flex" :justifyContent "center"}}
            (map display-item (:shop inventory)))

        (h2 {:style {:textAlign "center" :color "white"}} (str "Turn "  (:game-cp/turn game-state)))
        (button {:onClick #(comp/transact! this `[(next-turn ~{:id 1})
                                                  (refill-shop ~{:available-cards cards-in-deck :number 4})])} "Next Turn")

       (div {:style {:minHeight "300px"}})
       (dom/img {:src "itemcardstrash.svg"})
       
       
       
       
       
       
  )))


























#_(defmutation temp-modal [props]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :pet :pet/temp-modal] not)
          )
  )  


(defmutation toggle-map [props]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :pet :pet/show-map] not)
          )
  )  


(defmutation toggle-lvl-up-modal [{:keys [target]}]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :pet :pet/lvl-up-modal] #(if target true (not %)) )
          )
  )  

(defmutation switch-stage [{:keys [stage]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:component/id :pet :pet/stage] stage)
          )
  )  

;;;just for testing  -- TODO: game-id always 1
(defmutation gain-xp [{:keys [amount]}]
  (action [{:keys [state]}]
          (swap! state update-in [:game-cp/id 1 :game-cp/xp]  #(+ %  amount )  )
          ))  





(defmutation get-pet [{:keys [type name]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:game-cp/id 1 :game-cp/pet]  {:type type :name name :xp 0 :max-hp 100 :hp 100 :pw 5 :int 5 :sp 0 :lvl 1 :level-curve [100 200 300 400 500] :ups 0}  )
          )
  )  

;;; seems very hacky....
;; AND, TODO: add some random-ness!!!!!!
(defmutation generate-quest [{:keys [aufgabenliste]}]
  (action [{:keys [state]}]
          #_(js/console.log (str (map (fn [m] [(:aufgabe/id m) m]) (map #(comp/get-initial-state aufg/Aufgabe %) aufgabenliste) )))
          
          (swap! state assoc :aufgabe/id 
           (into {} (map (fn [m] [(:aufgabe/id m) m]) (map #(comp/get-initial-state aufg/Aufgabe %) aufgabenliste) ))     ;(map #(assoc %1 :id %2) n aufgaben-ids) 
          )                                      
          (swap! state assoc :aufg-actions/id 
           (into {} (map (fn [m] [(:aufg-actions/id m) m]) (map #(comp/get-initial-state aufg/AufgabenActions %) aufgabenliste) ))     ;(map #(assoc %1 :id %2) n aufgaben-ids) 
          )
          (swap! state assoc-in [:component/id :quest :quest/aufgabenliste] (vec (map (fn [m] [:aufgabe/id (:id m)]) aufgabenliste)) ;(map #(comp/get-initial-state aufg/Aufgabe %) aufgabenliste)
          )
          (swap! state update-in [:component/id :questboard :questboard/items] #(remove (fn [n] (= (:id (first (:aufg n))) (:id (first aufgabenliste)) ))  %)
          )
          
  )
)
(defmutation change-active-quest-task [{:quest/keys [active]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:component/id :quest :quest/active] active))
  )


#_(defn remove-firstXX [pred lst]
  (let [[before after]
        (loop [b [] a lst]
         (if (empty? lst)
          [b a]
          (if (pred (first a))
           [b (rest a)]
           (recur (conj b (first a)) (rest a)))))
         ]
          (concat after before) 
    ))
    
#_(js/console.log (str (vec (remove-firstXX (fn [n] (= (:action n) :a )) [{:action :b} {:action :a} {:action :a}]))))

(defmutation remove-plant-card [{:keys [td-action]}]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :pet :pet/cards]  #(update-in % [td-action :count] dec)    )
          ;(js/console.log (str "fuck you " (get-in (get-in @state [:component/id :pet :pet/cards]) [:red :count] ) ))
          
          )
  )

;;;TODO: calculate new wave    -note: id of tower could be different!!
(defmutation next-turn [{:keys [id]}]
  (action [{:keys [state]}]
          (swap! state update-in [:game-cp/id id :game-cp/turn] inc)
          (swap! state update-in [:tower/id id :tower/wave] #(merge % {:tiny-fishy 4}))
          )
  )

(defmutation toggle-shop-modal [{:keys [detailed modal card card2 card-action] :as props}]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :shop :shop/modal] #(assoc % :modal modal :card card :card2 card2 :detailed detailed :card-action card-action
                                                                      ;(case modal
                                                                      ;:deck {:modal :deck}
                                                                      ;:cards {:modal :cards :card card}
                                                                      ;:detailed {:modal :detailed :card card :card2 card2}
                                                                      ;false)
                                                                      ))
          )
  )  

(defmutation replace-deck-card [{:keys [replace add]}]
;;;;;;; TODO: Warning when someone removes all aufgaben-karten!! (currently, less than 2 is bad)
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :shop :shop/deck]  #(if (some #{add} (get-in @state [:component/id :shop :shop/deck]))
                                                                        (do (js/alert "Card is already in your deck!") (identity %))
                                                                        ;(do (js/console.log (str (get-in @state [:component/id :shop :shop/deck]  "fu"))) (identity %))
                                                                        (conj (remove #{replace} %) add))  )
          )
  )  

(defmutation shop-payment [{:keys [price id]}]
  (action [{:keys [state]}]
          ;(let [discount (get-in @state [:component/id :shop :shop/temp-stats :discount] 1)]
            (swap! state update-in [:game-cp/id id :game-cp/cash]  #(- %  price))
            ;)
          )
  )  

(defmutation unlock-card [{:keys [id k]}]
  (action [{:keys [state]}]
          (swap! state update-in [:game-cp/id id :game-cp/cards] #(update % (:cat (k card-sheet)) (fn [n] (conj n k))))
          (swap! state update-in [:game-cp/id id :game-cp/unlockable] #(update % (:cat (k card-sheet)) (fn [n] (disj n k))))
          
          )
  )

(defmutation add-quest [{:keys [k]}]
  (action [{:keys [state]}]
          (swap! state update-in [:component/id :questboard :questboard/items] #(conj % k))
          )
  )



(defn draw-hand [available-cards number]
 (let [
                     ran-a (shuffle (:a available-cards)) ;(shuffle (reduce conj (:a available-cards) (:b available-cards)))
                     ;ran-s (shuffle (:s available-cards))
                     ;ran-p (shuffle (:p available-cards))
                     rest-hand (shuffle (flatten (conj (drop 2 ran-a) (vec (:s available-cards)))))
                     hand (flatten (conj (take (min number 2) ran-a) (take (- number 2) rest-hand))) ;not a set yet
                     ] 
                     {:a (set (for [a (filter (fn [n] (= :a (:cat (n card-sheet)))) hand)] a)) 
                      :s (set (for [s (filter (fn [n] (= :s (:cat (n card-sheet)))) hand)] s)) }
                      
                      ))

;;;;; TODO remove insta-reward
(defmutation activate-card [{:keys [id card aufgaben-tracker available-cards draw]}]
  (action [{:keys [state]}]
          (if (:unique card) (swap! state update-in [:game-cp/id id :game-cp/cards] #(update % (:cat card) (fn [n] (disj n (:k card)))))                                                                      
           )
         (let [discount (get-in @state [:component/id :shop :shop/temp-stats :discount] 1)
               bonus-multi (get-in @state [:component/id :shop :shop/temp-stats :bonus] 1)
               single-counter (get-in @state [:component/id :shop :shop/temp-stats :single-counter] 0)
               inventory (get-in @state [:component/id :shop :shop/inventory] {:a #{:a1}})
               card-count (apply + (for [x inventory] (count (second x))))

               draw (or draw 1)

               cards-in-hand (linearize-cards inventory)
               cards-left-in-deck (shuffle (remove (set cards-in-hand) (linearize-cards available-cards)))
               
               ]
          (case (:cat card)
          :a (do  ;;;; CAUTION!!!! INSTA-REWARD!!!!!
                 (swap! state update-in [:game-cp/id id :game-cp/cash]  #(int (+ %  (* bonus-multi (:reward card)) ))  )
                 (swap! state update-in [:game-cp/id id :game-cp/xp]  #(+ %  (:xp card) )  )
                 (swap! state update-in [:component/id :shop :shop/inventory]  (fn [n] (update-in n [:a] #(disj % (:k card))))  )                                                         
                 ;(if (:single card)   ;; currently counting all a-cards
                 (swap! state update-in [:component/id :shop :shop/temp-stats :single-counter]  inc  )
                 ;)                                                         
                 
                 (let [aufgaben-count (count (:aufg card))
                       number-of-aufgaben (or aufgaben-count 1)
                       aufgaben-ids (for [x (range number-of-aufgaben)] (+ aufgaben-tracker x 1)) 
                       card-with-ids (update card :aufg (fn [n] (map #(assoc %1 :id %2) n aufgaben-ids) )  )
                       
                 ]
                 (swap! state update-in [:game-cp/id id :game-cp/aufgaben-id-tracker]  #(+ %  number-of-aufgaben)  )
                 (swap! state update-in [:component/id :questboard :questboard/items] #(conj % card-with-ids)))
              )

          :s (do (swap! state update-in [:component/id :shop :shop/inventory]  (fn [n] (update-in n [:s] #(disj % (:k card))))  )                                                                 
                 (if (get card :discount false)
                   (swap! state assoc-in [:component/id :shop :shop/temp-stats :discount]  (* discount (:discount card))  )                                                                 
                   )
                 (if (get card :bonus false) 
                   (swap! state assoc-in [:component/id :shop :shop/temp-stats :bonus]  (+ bonus-multi (:bonus card))  )                                                                 
                   )
                 (if (get card :reroll false)
                   (swap! state assoc-in [:component/id :shop :shop/inventory] (draw-hand available-cards card-count) )
                   )
                 (let [inventory (get-in @state [:component/id :shop :shop/inventory] {:a #{:a1}})]
                  (if (get card :draw false)
                   ;maybe warning when running out of deck?
                   (let [number (case (:draw card) 
                                  :byprice (or draw 1)
                                  :bysingles single-counter
                                  1  )]
                    (swap! state assoc-in [:component/id :shop :shop/inventory] (categorize-cards (flatten (conj (linearize-cards inventory) (take number cards-left-in-deck))))         #_(merge % (draw-hand cards-left-in-deck 2)) )
                   )) 
                  ) 
              )
          
          :p (do (swap! state update-in [:component/id :shop :shop/inventory]  (fn [n] (update-in n [:p] #(disj % (:k card))))  )
                 ;;; ALL POSSIBLE CARDS MUST ALREADY BE INITIALIZED IN PETS!!!!!
                 (swap! state update-in [:component/id :pet :pet/cards] #(update-in % [(:action card) :count] inc))
              )
          
          nil
          ))
          )
  )




(defmutation refill-shop [{:keys [available-cards same-turn number]}]
  (action [{:keys [state]}]
          (do 
           (if (not same-turn) (swap! state assoc-in [:component/id :shop :shop/temp-stats] {}))
           ;(if (not same-turn) (swap! state assoc-in [:component/id :shop :shop/bonus] 1))
           (swap! state assoc-in [:component/id :shop :shop/inventory] 
               (draw-hand available-cards number) 
          )))
          
  )  

