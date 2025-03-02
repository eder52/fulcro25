(ns app.ui.game-cp
  (:require
    ;[app.ui.components :as a-comp]

   ;[com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h2 h3 button b]]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]

   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
   ;[com.fulcrologic.fulcro-css.css :as css]
   ;[com.fulcrologic.fulcro.routing.dynamic-routing :as dr]

   ))


(def card-sheet
 {:p0 {:k :p0 :name "- egg -" :available true :price 10 :color "#FFF"  :cat :p :up-price 15 :unique true}
  :p1 {:k :p1 :name "blue plant" :available false :price 1 :color "#55F"  :cat :p :action :blue}
  :p2 {:k :p2 :name "green plant" :available true :price 2 :color "#5F5"  :cat :p :action :green}
  :p3 {:k :p3 :name "red plant" :available true :price 5 :color "#F55"  :cat :p :action :red}
  :p4 {}
  :p5 {}

  :a1 {:k :a1 :name "Indiv 1" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}
  :a2 {:k :a2 :name "Indiv 2" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}
  :a3 {:k :a3 :name "Indiv 3" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}
  :a4 {:k :a4 :name "Indiv 4" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}
  :a5 {:k :a5 :name "Indiv 5" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}
  :a6 {:k :a6 :name "Indiv 6" :available true :single true :price 0 :color "#D5A" :reward 1 :xp 1 :cat :a :aufg [{:typ :typP1}]}

  :b1 {:k :b1 :name "bundle 1" :available true :price 7 :color "#9DE" :reward 10 :xp 3 :cat :a :up-price 10 :aufg [{:typ :typP1} {:typ :typP1}]}
  :b2 {:k :b2 :name "bundle 2" :available true :price 7 :color "#9DE" :reward 10 :xp 3 :cat :a :up-price 10 :aufg [{:typ :typP1} {:typ :typP1}]}
  
  :s1 {:k :s1 :name "Reroll" :available true :reroll true :price 5 :up-price 10 :color "#C4F"  :cat :s}
  :s2 {:k :s2 :name "Discounts" :discount 0.5 :available true :price 5 :up-price 10 :color "#C4F"  :cat :s}
  :s3 {:k :s3 :name "Draw (pay)" :available true :draw :byprice :price 5 :up-price 10 :color "#C4F"  :cat :s}
  :s4 {:k :s4 :name "Duplicate" :available true :price 5 :up-price 10 :color "#C4F"  :cat :s}
  :s5 {:k :s5 :name "Bonus Reward" :bonus 0.5 :available true :price 5 :up-price 10 :color "#C4F"  :cat :s}
  :s6 {:k :s6 :name "Draw (rec)" :available true :draw :bysingles :price 3 :up-price 10 :color "#C4F"  :cat :s}
  :s7 {:k :s7 :name "Draw (rec)" :available true :draw :bysingles :price 3 :up-price 10 :color "#C4F"  :cat :s}
  
  :t1 {:k :t1 :name "NAME" :det "+1 Eco +1 Team" :available true :price 3 :color "#C4F"  :cat :t}
  

  })

  (def swipers 
   {1 "Aufgabe. -4$. +10XP"
    2 "   +1Eco"
    3 "   +1Speed"
    4 "   +1Luck"
    5 "   +1Team"
     })

  (def stages {1 {:stage 1 :xp 100}
               2 {:stage 2 :xp 200}
               3 {:stage 3 :xp 300}
               4 {:stage 4 :xp 400}
               5 {:stage 5 :xp 999999}})
  (defn stage-by-xp [xp]
    (:stage (first (filter (fn [n] (< xp (:xp n))) (vals stages) ))))  ;; doesn't check if xp exceeds last stage


(defn linearize-cards [cards]
 (flatten (conj (vec (:a cards)) (vec (:s cards)))))

(defn categorize-cards [cards]
 {:a (set (for [a (filter (fn [n] (= :a (:cat (n card-sheet)))) cards)] a)) 
  :s (set (for [s (filter (fn [n] (= :s (:cat (n card-sheet)))) cards)] s)) })

(defn xp-to-level [xp progression]
  (reduce (fn [l x] (if (> xp x) (inc l) l)) 1 progression) )

(def pet-sheet 
  {:a {:level-curve (list 100 200 300 400 500)}})

(defn pet-level [type xp]
  (reduce (fn [l x] (if (> xp x) (inc l) l)) 1 (get-in pet-sheet [type :level-curve])))




(defsc TowerCP [this {:tower/keys [id size-x size-y grid wave] :as props}]
  {:query         [:tower/id :tower/wave :tower/grid :tower/size-x :tower/size-y :tower/bx :tower/by :tower/bait :tower/wave-queue]
   :initial-state {:tower/id :param/id
                   :tower/size-x :param/size-x
                   :tower/size-y :param/size-y
                   :tower/grid :param/grid
                   :tower/wave :param/wave
                   :tower/bx :param/bx
                   :tower/by :param/by
                   :tower/bait 0
                   :tower/wave-queue 10
                   }
   :ident         :tower/id
   }
  )


(defn build-tower-defense-grid [sx sy base-x base-y base-size-x base-size-y]
  
  ; multiple base-sizes propaply by reducing over all the base-coordinates??
  (-> (vec (for [y (range 15)] (vec (repeat 30 [:lane :empty]))))
    (assoc-in [base-y base-x 0] :base)
    (assoc-in [(inc base-y) base-x 0] :base)
    (assoc-in [base-y (inc base-x) 0] :base)
    (assoc-in [(inc base-y) (inc base-x) 0] :base)
    
  )

  )

(defsc GameCP [this {:game-cp/keys [id cards] :as props}]
  {:query         [:game-cp/id :game-cp/turn :game-cp/cards :game-cp/cash :game-cp/unlockable 
                   :game-cp/aufgaben-id-tracker :game-cp/xp :game-cp/settings :game-cp/pet {:game-cp/td1 (comp/get-query TowerCP)}]
   :initial-state {:game-cp/id :param/id
                   :game-cp/turn 0
                   :game-cp/xp 0
                   :game-cp/pet {}
                   ;TODO - if i resize the grid, i need to keep all planted plants
                   ;TODO - currently the base NEEDS to be size 2x2 !!!!
                   :game-cp/td1 {:id 1 :wave {} :size-x 30 :size-y 15 :bx 2 :by 5 :grid (build-tower-defense-grid 30 15 2 5 2 2)}
                   
                   :game-cp/settings {:id 1 :card-action :zoom}
                   :game-cp/aufgaben-id-tracker 0
                   :game-cp/cards {:a #{:a1 :a2 :a3 :a4 :a5 :a6} :p #{} :s #{}} ;; add all to initialize set-type!
                   :game-cp/unlockable {:p #{:p1} :a #{:b1 :b2} :s #{:s1 :s2 :s3 :s4 :s5 :s6 :s7}}
                   :game-cp/cash 75}
   :ident         :game-cp/id
   }
  )
(def ui-game-cp (comp/factory GameCP))  ;; probably not used anywhere



(defn add-xp [pet xp]
 (let [level-before (:lvl pet)
       old-xp (:xp pet)
       new-xp (+ old-xp xp)
       new-lvl (xp-to-level new-xp (:level-curve pet))
       lvl-jump (- new-lvl (:lvl pet))
       new-pet (assoc pet :xp new-xp)
       ]
           (if (< lvl-jump 1) 
               new-pet
               (-> new-pet
                   (assoc :lvl new-lvl)
                   (update :sp #(+ % lvl-jump))
                   (update :ups #(+ % lvl-jump)))
            )
))



;;; REPLACED!! only used for debug!!!
(defmutation gain-pet-xp [{:keys [game-id amount]}]
  (action [{:keys [state]}]
          (swap! state update-in [:game-cp/id game-id :game-cp/pet :xp]  #(+ %  amount )  )
          (let [pet (get-in @state [:game-cp/id game-id :game-cp/pet] {})
                level-before (:lvl pet)
                new-xp (:xp pet)
                new-lvl (xp-to-level new-xp (:level-curve pet))
                lvl-jump (- new-lvl (:lvl pet))]
           (if (> lvl-jump 0) 
               (do 
               (swap! state assoc-in [:game-cp/id game-id :game-cp/pet :lvl]  new-lvl)
               (swap! state update-in [:game-cp/id game-id :game-cp/pet :sp]  #(+ % lvl-jump))
               (swap! state update-in [:game-cp/id game-id :game-cp/pet :ups]  #(+ % lvl-jump))
              
               ;;; TODO: get to choose upgrades!!!!
               ))

          ;(js/console.log (str (get-in @state [:game-cp/id game-id :game-cp/pet] "fail")))
          ))
  )  


  (defmutation gain-pet-stats [{:keys [game-id stat amount]}]
   (action [{:keys [state]}]
          (swap! state update-in [:game-cp/id game-id :game-cp/pet]  (fn [s] (update s stat #(+ % amount) ))  )
          (swap! state update-in [:game-cp/id game-id :game-cp/pet :ups]  (fn [s] (max (dec s) 0) ))  
          )
  )  