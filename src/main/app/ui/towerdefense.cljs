(ns app.ui.towerdefense
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro-css.css :as css] 
    [app.ui.game-cp :as game]
   
   
   ))

(defn grid-area [px py aw ah size]
  (dom/rect {:x (str (* px size)) :y (str (* size py)) :rx "5px" :ry "5px" :width (str (* size aw)) :height (str (* size ah)) :fill "rgba(250,250,250, 0.2)" :stroke "white" :strokeWidth "2px"})
       )


(defn grid-unit2 [px py k size]
  (let [selected (= (second k) :selected)
        selected false] ;;; DEACTICATED
    (dom/rect {;:onClick (fn []  (js/alert (str px " " py)))
               :x (str (+ 1 (* px size))) :y (str (+ 1 (* size py))) :rx "2px" :ry "2px" :width (str (- size 2)) :height (str (- size 2)) 
               :fill "rgb(250,250,250)" :stroke "black" :strokeWidth "1px"
               :fillOpacity (if selected "20%" "0%")})
  )     
  )
(defn grid-unit [px py k size]
  (let [invisible (= (second k) :empty)
        color (if invisible "white" (str (symbol (second k))))
        ]
    (dom/rect {;:onClick (fn []  (js/alert (str px " " py)))
                           
               
               :x (str (* px size)) :y (str (* size py)) :rx "2px" :ry "2px" :width (str size) :height (str size) 
               :fill color :stroke "none" :strokeWidth "1px"
               :fillOpacity (if invisible "0%" "40%")})
  )     
  )

#_(defn find-base [grid]
  (loop [x 0
         y 0]
      (if (some  #(= :base (first %)) (nth grid y))
          (loop [n x]
            (if (= :base (get-in grid [y n 1]))  
              [y x]
              (recur (inc x))
            )
            )
          (recur x (inc y))
          )

    )
 )




#_(swap! state* assoc :polling-id (polling-request handler timeout))
;; This will store the id of the interval in state*
;; then on `:component-will-unmount` you can/should clear that with 
#_(js/clearInterval (:polling-id @state*))
#_(js/setInterval handler 500)

#_(reset! timeratom (js/setInterval (fn [] (swap! temp-pos inc)) 500))
#_(if @timeratom (js/clearInterval @timeratom))
#_(def timeratom (atom nil))








(let [timeratom (atom nil)]
 (defsc TowerDefenseRenderer [this {:tower-render/keys [id unit enemies action game-state] :as props}] 
  {:query [:tower-render/id :tower-render/unit :tower-render/enemies :tower-render/action 
           {:tower-render/game-state (comp/get-query game/GameCP)}]
   :ident :tower-render/id 
   :initial-state {:tower-render/id :param/id
                   :tower-render/unit :param/unit
                   ;:tower-render/sx :param/sx
                   ;:tower-render/sy :param/sy
                   :tower-render/game-state {:id 1}
                   :tower-render/enemies {1 {:id 1 :pos 0 :cell-changed false :sp 0.1 :spawn :right :lane 0 :size 1.2 :hp 5}
                                   2 {:id 2 :pos 0 :cell-changed false :sp 0.16 :spawn :right :lane 1 :size 0.8 :hp 5}}
                   :tower-render/action :nothing
                   }}
  
 (dom/div {:style { ;:position "absolute" 
                      ;:left (str good-x-start "px") ;(str (* 2 unit) "px") 
                      ;:top (str (* 4 unit) "px")
                      }
           :id "container-for-tower-defense"

           }
  (let [sx (get-in game-state [:game-cp/td1 :tower/size-x])
        sy (get-in game-state [:game-cp/td1 :tower/size-y])

        parent-div (js/document.getElementById "container-for-tower-defense")
        rect (if parent-div (.getBoundingClientRect parent-div) nil)
        y-start (if rect (.-top rect) 0)
        x-start (if rect (.-left rect) 0)
        ;_ (js/console.log (str (.-top rect) " " (.-left rect) " " (.-right rect) " " (.-bottom rect) " " (.-x rect)))
    
        ;window-width (.-clientWidth js/document.body)
        ;good-x-start (/ (- window-width (* unit sx)) 2)
        ;convert-eX-to-cellX (fn [x] (int (/ (- x good-x-start) unit)))
        convert-eX-to-cellX (fn [x] (int (identity (/ (- x x-start ) unit))))
        convert-eY-to-cellY (fn [y] (int (identity (/ (- y y-start ) unit))))
        
        grid (get-in game-state [:game-cp/td1 :tower/grid])
        wave-queue (get-in game-state [:game-cp/td1 :tower/wave-queue] 0)
        



        base-size [2 2]
        directions [1 0 0 0]

        base-x (get-in game-state [:game-cp/td1 :tower/bx])
        base-y (get-in game-state [:game-cp/td1 :tower/by])
        ;;base-x 1;(second (find-base grid)) ; if wave only coming from right side 
        ;;base-y 5;(first (find-base grid)) ; if wave only from one direction
        #_#_lanes {:rechts [base-x 1]
               :unten []
               :links []
               :oben []}
        ;(cycle base-size)

        ]
        
    
    
     (dom/svg #js {:width (* unit sx) :height (* unit sy)}

       ;grid     
             (if (not (= action :nothing))
               (for [x (range sx)
                   y (range sy)]
               (grid-unit2 x y  (get-in grid [y x]) 
                          unit)))
       ;draw towers       
              (for [x (range sx)
                   y (range sy)]
               (grid-unit x y  (get-in grid [y x]) 
                          unit))

       ;base  -- TODO: DYNAMIC POSITIONING!!!
             (grid-area base-x base-y (first base-size) (second base-size) unit)
              

        ;enemy-list
        (dom/text {:x "600" :y "80" :fill "white"} (str  "tiny fishy: " (:tiny-fishy (:tower/wave (:game-cp/td1 game-state))))) 
        

        ;enemies -- TODO:  FIX LANES!!! (also in mutations)
              (for [enemykey (keys enemies)]
               (let [enemy (get enemies enemykey)]
                (if (pos? (get enemy :hp 1)) 
                  (dom/circle {:cx (str (* unit (- sx (:pos enemy) ))) 
                               :cy (str (* unit (+ base-y 0.5 (get enemy :lane)))) 
                               :r (str (* 15 (get enemy :size) (get enemy :hp 1) 0.4) )  :fill "rgb(180,255,180)"})
                ) ) 
                )
              

        ;whole interactable area (overlay)
             (let [bait (if (= action :bait) 1 0)]
              (dom/rect #js {:onClick (fn [e] (if (= action :nothing) nil (do (comp/transact! this `[(change-cell ~{:tower/id id :xpos (convert-eX-to-cellX (.-clientX e)) :ypos (convert-eY-to-cellY (.-clientY e)) :new-entry action :bait bait})])
                                                                             (comp/transact! this `[(change-action ~{:tower-render/id id :new-action :nothing})]))))
                            :width (* unit sx) :height (* unit sy) :style #js {:fill        "rgba(200,200,200, 0.05)"
                                                                               ;:strokeWidth 2
                                                                               :stroke      "none"}}))
              
         ;((playbutton))
              (dom/rect #js {:onClick (fn [] ;(comp/transact! this `[(move-enemies ~{:tower/id id})])
                                        (comp/transact! this `[(load-new-wave ~{:tower-render/id id })])
                                        (if @timeratom (do (js/clearInterval @timeratom) (reset! timeratom false))
                                                 (reset! timeratom (js/setInterval (fn [] (do (comp/transact! this `[(move-enemies ~{:tower-render/id id :tower-render/grid grid :tower-render/sx sx :tower-render/base-y base-y :game-id 1})
                                                                                                                    ;(collect-xp ~{:game-id id :amount 10})
                                                                                                                     ;(game/gain-pet-xp ~{:game-id id :amount 10})
                                                                                                                     (eventually-end-movement ~{:tower-render/id id :tower-render/timeratom timeratom :game-id (:game-cp/id game-state)})
                                                                                                                     ])
                                                                                                                ) ) 
                                                                                   90))
                                                 ) 
                                               )
                            :x (* unit (- sx 3 )) :y (* unit (- sy 2 ))
                            :width (* unit 2) :height (* unit 1) :style #js {:fill        "rgba(200,200,200, 0.3)"
                                                                               :strokeWidth 2
                                                                               :stroke      "black"}})
              (dom/text {:x (* unit (- sx 3 )) :y (* unit (- sy 2 )) :fill "white"} (str "waves: " wave-queue))



          ;((enemy infolabel))
                 

     )
    

    )
  
  
  
  )))

(def ui-towergame (comp/factory TowerDefenseRenderer))














;; does not check for uberschreiben!!
(defmutation change-cell [{:tower/keys [id] :keys [xpos ypos new-entry bait]}]
  (action [{:keys [state]}]
          (swap! state update-in [:tower/id id :tower/grid] #(assoc-in % [ypos xpos 1] new-entry))
          (when bait
           (swap! state update-in [:tower/id id :tower/bait] #(+ % bait)))
          )
  )

(defmutation change-action [{:tower-render/keys [id ] :keys [new-action]}]
  (action [{:keys [state]}] 
          (swap! state assoc-in [:tower-render/id id :tower-render/action] new-action))
  )


(defmutation eventually-end-movement [{:tower-render/keys [id timeratom] :keys [game-id]}]
  (action [{:keys [state]}] 
          (let [enemies (get-in @state [:tower-render/id id :tower-render/enemies] {})
                dead-params (for [x (vals enemies)] (:dead x)) 
                all-dead? (every? true? dead-params)]
          (if all-dead?
              (do 
                (js/clearInterval @timeratom) 
                (reset! timeratom false) 
                (js/console.log "wave end")
                (let [xp (apply + (for [x (vals enemies)] (:xp x)))]
                  (swap! state update-in [:game-cp/id game-id :game-cp/pet]  #(game/add-xp % xp))
                  )
              )
          )))
  )


;; TODO: not always the same wave

(defn generate-wave [bait]
  (let [wave  {1 {:id 1 :dead false :pos 0 :cell-changed false :sp 0.2 :pw 2 :spawn :right :lane 0 :size 1.2 :hp 5 :xp 20}
               2 {:id 2 :dead false :pos 0 :cell-changed false :sp 0.4 :pw 1 :spawn :right :lane 1 :size 0.8 :hp 5 :xp 10}}
        add1  (when (> bait 0) {3 {:id 3 :dead false :pos -3 :cell-changed false :sp 0.2 :pw 4 :spawn :right :lane 0 :size 0.5 :hp 20 :xp 20}} )     
        add2  (when (> bait 1) {4 {:id 4 :dead false :pos 1 :cell-changed false :sp 0.5 :pw 2 :spawn :right :lane 0 :size 0.5 :hp 15 :xp 20}} )     
               ]
 
   (merge wave add1 add2 )
   
   )
   
   )

(defmutation load-new-wave [{:tower-render/keys [id ]}]
  (action [{:keys [state]}] 
          (swap! state assoc-in [:tower-render/id id :tower-render/enemies] (generate-wave (get-in @state [:tower/id id :tower/bait] 0)) )
          (swap! state update-in [:tower/id id :tower/wave-queue] #(max 0 (dec %)) )
          )
  )





(defmutation move-enemies [{:tower-render/keys [id sx grid base-y] :keys [game-id]}]
  (action [{:keys [state]}]
          (let [pet (get-in @state [:game-cp/id game-id :game-cp/pet] {})
                enemies (get-in @state [:tower-render/id id :tower-render/enemies] {})]
            (swap! state update-in [:tower-render/id id :tower-render/enemies] 
                 (fn [s]
                   (apply merge
                          (for [x (keys s)]
                            (let [before (get s x)]
                              (if (get before :dead false) {x before}
                                  (let [old-position (:pos before)
                                        new-position (+ old-position (get before :sp 1))
                                        after-move (assoc before :pos new-position)
                                        cell-changed? (not (= (int new-position) (int old-position)))
                                        after-cell-flag (assoc after-move :cell-changed cell-changed?)
                         ;;; VERY NOT GOOD!!!!! figure out how to track location!!
                                        grid-position (case (:spawn before) 
                                                            ;add bit of randomness to y
                                                            :right [(+ base-y (:lane after-move)) (- sx (int new-position) 1)]
                                                            ; add other directions
                                                            [(+ base-y (:lane after-move)) (- sx (int new-position) 1)]
                                                            )
                                        check-position (get-in grid grid-position)
                                        ;; TODO: turn 'base' into vector,so you can put plants in base (kinda did that)
                                        received-damage (cond (= (first check-position) :base) (:pw pet)
                                                              cell-changed? (case (second check-position) :red 5 :green 1 :blue 1 0)
                                                              :else 0)
                                        new-hp (max 0 (- (:hp before) received-damage))
                                        after-damage (assoc after-cell-flag :hp new-hp)
                                        after-death-and-base-check (let [dead? (< new-hp 1)
                                                                         base? (= (first check-position) :base)
                                                                         speed (if base? 0 (:sp before))]
                                                                         (assoc after-damage :dead dead? :sp speed)
                                          
                                         )
                                        ]
                                    {x after-death-and-base-check})))))
                   ))
                   
            (let [damages (for [x (vals enemies) :when (and (not (:dead x)) (= 0 (:sp x)))] (:pw x) )
                  damage-total (apply + damages)] 
                  
                  (when (> damage-total 0)
                   (js/console.log (str "--" damage-total))
                   (js/console.log (str "- -" damages))
                   
                   (swap! state update-in [:game-cp/id game-id :game-cp/pet :hp] (fn [hp] (- hp damage-total)))
              ))
            
            
            )
  )
)

