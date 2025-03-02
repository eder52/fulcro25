(ns app.ui.aufgaben
  (:require
   
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h2 h3 button b]] 
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro-css.css :as css] 
    
    ))


(defn arrow1 [transform thickness] (dom/g transform (dom/path {:d "M0.871,20.318C32.345,0.074 66.731,2.016 98.761,19.902" :fill "none" :stroke "white" :strokeWidth (str thickness "px")})
                       (dom/path {:d "M96.836,14.041L98.761,19.902L93.301,22.093" :fill "none" :stroke "white" :strokeWidth (str thickness "px")})))
(defn arrow2 [transform thickness] (dom/g transform (dom/path {:d "M0.871,20.318C25.368,0.932 59.999,13.432 57.564,27.196C55.986,36.115 41.237,36.922 38.755,26.885C35.245,12.692 73.694,0.089 98.761,19.902" :fill "none" :stroke "white" :strokeWidth (str thickness "px")})
                       (dom/path {:d "M96.836,14.041L98.761,19.902L93.301,20.984" :fill "none" :stroke "white" :strokeWidth (str thickness "px")})))




#_(defsc DreieckActions [this {:drei-actions/keys [id active] :as props}]
  {:query [:drei-actions/id  :drei-actions/active]
   :ident :drei-actions/id 
   :initial-state {:drei-actions/id :param/id 
                   :drei-actions/active  :notool 
                   }}
  (let [color "white"]
    (div
     (dom/svg {:width "200" :height "400" :xmlns "http://www.w3.org/2000/svg"}

              (dom/g {:transform "translate(5 0)"}
                     (dom/path {:d "M44.608,9.92C44.608,9.92 50.752,12.914 53.204,20.181C57.641,13.941 64.826,6.719 69.704,5.483" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x      
              (dom/g {:transform "translate(5 0)"}
                     (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                     (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x
              (dom/g {:transform "translate(5 45)"}
                     (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                     (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))

              (dom/text {:x "50" :y "65" :fill color} "S+W")
              (dom/text {:x "50" :y "95" :fill color} "S+W/S")
              (dom/text {:x "100" :y "95" :fill color} "KoSa")


              (dom/path {:d "M65,30 L65,45" :fill "none" :stroke color :strokeWidth "2px"})
              (dom/path {:d "M110,30 L110,45" :fill "none" :stroke color :strokeWidth "2px"})
              (dom/path {:d "M100,30 L75,45" :fill "none" :stroke color :strokeWidth "2px"})
              (dom/path {:d "M65,70 L65,80" :fill "none" :stroke color :strokeWidth "2px"})
              (dom/path {:d "M110,70 L110,80" :fill "none" :stroke color :strokeWidth "2px"})


        ;triangle, two lupen      
              (dom/path {:d "M4.278,2.155L4.278,28.244L22.381,15.022L4.278,2.155Z" :fill "none" :stroke color :strokeWidth "2px"})
              (dom/g {:transform "translate(0 15)"}
                     (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))
              (dom/g {:transform "translate(0 50)"}
                     (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))

              

              (dom/rect { :x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none" :cursor "pointer"})
              
              ))
  )
)
#_(def ui-drei-actions (comp/factory DreieckActions {:keyfn :drei-actions/id} ))



(defn dreiecktool [color]
      (let [rectangle1 (dom/rect { :x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(0,20,100)" :fillOpacity "30%" :stroke "none" :cursor "pointer"})
            rectangle2 (dom/rect { :x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(0,20,100)" :fillOpacity "0%" :stroke "none" :cursor "pointer"})
              ] 
        (dom/g
         ;rectangle1
         (dom/g {:transform "translate(5 0)"}
                (dom/path {:d "M44.608,9.92C44.608,9.92 50.752,12.914 53.204,20.181C57.641,13.941 64.826,6.719 69.704,5.483" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x      
         (dom/g {:transform "translate(5 0)"}
                (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x
         (dom/g {:transform "translate(5 45)"}
                (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))

         (dom/text {:x "50" :y "65" :fill color} "S+W")
         (dom/text {:x "50" :y "95" :fill color} "S+W/S")
         (dom/text {:x "100" :y "95" :fill color} "KoSa")


         (dom/path {:d "M65,30 L65,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M110,30 L110,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M100,30 L75,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M65,70 L65,80" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M110,70 L110,80" :fill "none" :stroke color :strokeWidth "2px"})


        ;triangle, two lupen      
         (dom/path {:d "M4.278,2.155L4.278,28.244L22.381,15.022L4.278,2.155Z" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/g {:transform "translate(0 15)"}
                (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))
         (dom/g {:transform "translate(0 50)"}
                (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))



         (dom/rect {:x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
         ;rectangle2
         
         )))

#_(defn formeltool []
  (let []
    (dom/g 
     (dom/g {:transform "translate(70 0)"}
                     (dom/path {:d "M5,50L30,15L55,50L5,50Z" :fill "none" :stroke "white" }))
     (dom/circle {:cx "35" :cy "30" :r "20" :stroke "white" :fill "none"})
     (dom/rect {:x "10" :y "65" :width "50" :height "30" :stroke "white" :fill "none"})
     (dom/rect {:x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
    )))

(defn parabeltool []
  (let []
    (dom/g 
     
     (dom/rect {:x "0" :y "0" :width "120" :height "45" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
     (dom/rect {:x "0" :y "50" :width "120" :height "45" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
     (dom/rect {:x "0" :y "100" :width "120" :height "45" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
    )))

(defn select-extension [this id]
  (let []
    (dom/g 
     (dom/path {:d "M10,22L 10,52L 30,37L 10,22Z" :fill "none" :stroke "white" :strokeWidth "2px"}) 
     (dom/rect {:onClick #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :selectX})])
                :x "0" :y "0" :width "45" :height "70" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
     )))


(defn modaltool [this id content]
  (div {:style {:overflow "auto" :backgroundColor "rgba(0,0,80,0.5)" :position "absolute" :display "block" :zIndex 5 :width "90%" :height "100%" :left "0" :top "0"}}
       (dom/button {:onClick #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :notool})])
                    :style {:fontSize "3em" :color "white" :backgroundColor "rgb(80,80,150)" :position "absolute" :left "95%"}} "X")
       
       content
       )
  )

(defn parabel-aufgaben []
  (let []
    (div
     (dom/p "Die Parabel p verläuft durch die Punkte" (dom/span  " P(6|6) ")
             "und" (dom/span  " Q(8|3) ")
             ". Sie hat die Gleichung der Form" (dom/span  " y=-0,25x²+bx+c ")
             "(b, c, x, y \u220A IR).")
      (dom/p "Zeigen Sie durch Berechnung der Werte für b und c, dass die Parabel die Gleichung y=-0,25x²+2x+3 hat.")
      
     (dom/p "Ermitteln Sie rechnerisch, für welche Belegungen von x es Dreiecke A" 
              (dom/sub "n") "B" (dom/sub "n") "C" (dom/sub "n") " gibt.")
    
    (dom/p "Zeigen Sie durch Rechnung, dass für die Länge der Strecken " (dom/span  {:style {:borderTop "1px solid black"}} "A" (dom/sub "n") "C" (dom/sub "n")) " in Abhängigkeit von x gilt: "
             "|" (dom/span {:style {:borderTop "1px solid black"}} "A" (dom/sub "n") "C" (dom/sub "n")) "|(x) = (-0,25x²+1,8x+4) LE."
              ) 

     (dom/p "Ermitteln Sie die maximale Streckenlänge |"  (dom/span  {:style {:borderTop "1px solid black"}} "A" (dom/sub "0") "C" (dom/sub "0"))
      "| sowie den zugehörigen Wert für x. " (dom/br)
      "Berechnen Sie sodann den maximalen Flächeninhalt A" 
      (dom/sub "max") " der Dreiecke A" (dom/sub "n") "B" (dom/sub "n") "C" (dom/sub "n") "."
      
      )          
    
    )
 ))


(defsc AufgabenActions [this {:aufg-actions/keys [id active] :as props}]
  {:query [:aufg-actions/id ;:aufg-actions/toolbox 
           :aufg-actions/active]
   :ident :aufg-actions/id 
   :initial-state ;(fn [{:keys [id]}] 
                   {:aufg-actions/id :param/id
                   ;:aufg-actions/toolbox  :param/toolbox
                   :aufg-actions/active  :notool 
                   }
                    ;)    
    }
  (let [highlight-box (fn [y action opa] (dom/rect {:onClick   #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active action})]) :x (str 5) :y (str y) :width "110" :height "90" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity opa :stroke "none" :cursor "pointer"}))
        color "white"]
    (div {:onKeyDown (fn [e] (case (.-key e)
                                     "1"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :select})])
                                     "2"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :insert})])
                                     "3"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :solve})])
                                     "4"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :parab})])
                                     "5"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :drei})])
                                     "6"   (comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active :formel})])
                             nil))
        :tabIndex "1"}
     (dom/svg {:width "300" :height "600" :xmlns "http://www.w3.org/2000/svg"}
              #_(dom/rect  {:onClick   #(comp/transact! this `[(aufgabenactionsucksbigtime ~{:aufg-actions/id id})])
                            :x 20 :y 20 :width "55" :height "55" :style  {:fill "rgb(200,200,200)"
                                                                          :strokeWidth "2"}})


              (dom/rect {:x "25" :y "30" :rx "5px" :ry "5px" :width "70" :height "40" :fill "none" :stroke "white" :strokeWidth "3px"})
              ;(dom/rect {:onClick   #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active (case active :select :notool :select)})]) :x "5" :y "5" :width "110" :height "90" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none"})
              (highlight-box 5 (case active :select :notool :select) (case active :select "20%" "5%"))

              (arrow1 {:transform "translate (15 135) scale(0.9,0.9)"} 3)
              ;(dom/rect {:onClick   #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active (case active :insert :notool :insert)})]) :x "5" :y "100" :width "110" :height "90" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none"})
              (highlight-box (+ 5 (* 95 1)) (case active :insert :notool :insert) (case active :insert "20%" "5%"))

              (arrow2 {:transform "translate (15 225) scale(0.9,0.9)"} 3)
              ;(dom/rect {:onClick   #(comp/transact! this `[(change-active-action ~{:aufg-actions/id id :aufg-actions/active (case active :solve :notool :solve)})]) :x "5" :y "195" :width "110" :height "90" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none"}) 
              (highlight-box (+ 5 (* 95 2)) (case active :solve :notool :solve) (case active :solve "20%" "5%"))


              (highlight-box (+ 5 (* 95 3)) (case active :parab :notool :parab) (case active :parab "20%" "5%"))


              (dom/g {:transform "translate(30 400)"}
                     (dom/path {:d "M5,50L30,15L55,50L5,50Z" :fill "none" :stroke "white" :strokeWidth "2px"}))
              (highlight-box (+ 5 (* 95 4)) (case active :drei :notool :drei) (case active :drei "20%" "5%"))

              (dom/g {:transform "translate(20 400) scale(3 3)"}
                     (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke "white" :strokeWidth "1px"}))

              (highlight-box (+ 5 (* 95 5)) (case active :formel :notool :formel) (case active :formel "20%" "5%"))



              (if (= active :drei) (dom/g {:transform "translate(120 380)"} (dreiecktool color)))
              ;(if (= active :formel) (dom/g {:transform "translate(120 470)"} (formeltool)))
              (if (= active :parab) (dom/g {:transform "translate(120 260)"} (parabeltool)))
              (if (= active :select) (dom/g {:transform "translate(120 15)"} (select-extension this id)))
              
              
              
              )
              
              (case active 
                  :selectX (modaltool this id (parabel-aufgaben)) 
                  :formel (modaltool this id nil) 
                  nil)
   
              ))
)

(def ui-aufg-actions (comp/factory AufgabenActions {:keyfn :aufg-actions/id} ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

         ;;;;;;
        ;;;;;;;;
       ;;;;  ;;;;
      ;;;;    ;;;;
     ;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;
   ;;;;          ;;;;
  ;;;;            ;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;







(defn render-aufgabeP1 [id action clip state this]
  (let [;action (:aufg-actions/active actions)
        select-p1 (case action :select {:onClick #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :p1})]) :style {:cursor "pointer"}} {})
        select-p2 (case action :select {:onClick #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :p2})]) :style {:cursor "pointer"}} {})
        select-eq (case action :select {:onClick #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :eq})]) :style {:cursor "pointer"}} {})
        put-left  (case clip :p1 (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :p1})]))
                             :p2 (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :p2})]))
                                 (fn []))
        put-right (case clip :eq (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :eq})])) (fn []))
        clip-p1   (case action :insert #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :p1})]) (fn []))
        clip-p2   (case action :insert #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :p2})]) (fn []))


        clip-s1 (case action :insert #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :s1})]) false)
        clip-s2 (case action :insert #(comp/transact! this `[(aufgabe-clip ~{:aufgabe/id id :content :s2})]) false)
        insert-p1 (if (and (= action :insert) (= clip :p1)) #(comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :i1})]) false)
        insert-p2 (if (and (= action :insert) (= clip :p2)) #(comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :i2})]) false)
        solve-1 (if (and (= action :solve) (contains? state :i1)) (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :s1})]))  false)
        solve-2 (if (and (= action :solve) (every? state [:i2 :i3])) (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :s2})]))   false)
        solve-3 (if (and (= action :solve) (every? state [:i2 :i3 :i4])) (fn [] (comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content '(:s3 :complete)})]))   false)
        insert-s1 (if (and (= action :insert) (= clip :s1)) #(comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :i3})]) false)
        insert-s2 (if (and (= action :insert) (= clip :s2)) #(comp/transact! this `[(aufgabe-nextstate ~{:aufgabe/id id :content :i4})]) false)

        eq1-events (or insert-p1 solve-1 (fn []))
        eq2-events (or insert-p2 insert-s1 solve-2 (fn []))
        
        s1-events (or solve-3 insert-s2 clip-s1   (fn []))
        s2-events (or clip-s2    (fn []))

        ]

    
     (dom/div 
      (dom/h2 "Aufgabe " (str id))

      (dom/p "Die Parabel p verläuft durch die Punkte" (dom/span select-p1 " P(6|6) ")
             "und" (dom/span select-p2 " Q(8|3) ")
             ". Sie hat die Gleichung der Form" (dom/span select-eq " y=-0,25x²+bx+c ")
             "(b, c, x, y \u220A IR).")
      (dom/p "Zeigen Sie durch Berechnung der Werte für b und c, dass die Parabel die Gleichung y=-0,25x²+2x+3 hat.")
      

      (dom/svg {:width "600" :height "500" :xmlns "http://www.w3.org/2000/svg"}
               (if (= action :select) (dom/rect {:onClick put-left :x "5" :y "5" :width "250" :height "450" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none"}))
               (if (= action :select) (dom/rect {:onClick put-right :x "260" :y "5" :width "250" :height "450" :fill "rgb(223,239,255)" :fillOpacity "10%" :stroke "none"}))
               (if (contains? state :p1)
                 (dom/g (dom/text {:x "30" :y "45" :fill "white"} "P(6|6)")
                        (dom/rect {:onClick clip-p1 :cursor (case action :insert "pointer" "auto") :x "20" :y "20" :width "60" :height "40" :fillOpacity "0%" :rx "5" :ry "5" :stroke "white"})))
               (if (contains? state :p2)
                 (dom/g (dom/text {:x "30" :y "265" :fill "white"} "Q(8|3)")
                        (dom/rect {:onClick clip-p2 :cursor (case action :insert "pointer" "auto") :x "20" :y "240" :width "60" :height "40" :fillOpacity "0%" :rx "5" :ry "5" :stroke "white"})))
               (if (contains? state :eq)
                 (dom/g (dom/text {:x "285" :y "45" :fill "white"} "y=-0,25x²+bx+c")
                        (dom/rect {:onClick eq1-events :x "275" :y "20" :width "120" :height "40" :fillOpacity "0%" :rx "5" :ry "5" :stroke "white"})))
               (if (every? state [:p2 :eq])
                 (dom/g (dom/text {:x "285" :y "265" :fill "white"} "y=-0,25x²+bx+c")
                        (dom/rect {:onClick eq2-events :x "275" :y "240" :width "120" :height "40" :fillOpacity "0%" :rx "5" :ry "5" :stroke "white"})))
               (if (contains? state :i1) (arrow1 {:transform "translate (130 20)"} 1))
               (if (contains? state :i2) (arrow1 {:transform "translate (130 240)"} 1))
               (if (contains? state :i3) (arrow1 {:transform "translate(350 180) rotate(90) scale(0.5,0.5)"} 2))
               (if (contains? state :i4) (arrow1 {:transform "translate(260 365) rotate(-90) scale(2,2)"} 0.5))


               (if (contains? state :s1) (arrow2 {:transform "translate(350 70) rotate(90) scale(0.6,0.6)"} 2))
               (if (contains? state :s2) (arrow2 {:transform "translate(350 290) rotate(90) scale(0.6,0.6)"} 2))
               (if (contains? state :s3) (arrow2 {:transform "translate(360 140) scale(0.6,0.6)"} 2))

               (if (contains? state :s1) (dom/circle {:onClick s1-events :cx "340" :cy "155" :r "15" :cursor "pointer" :fill "rgb(180,255,180)"} 1))
               (if (contains? state :s2) (dom/circle {:onClick s2-events :cx "340" :cy "385" :r "15" :cursor "pointer" :fill "rgb(180,255,180)"} 1))
               (if (contains? state :s3) (dom/circle {;:onClick (or clip-s1 (fn []))
                                                      :cx "450" :cy "155" :r "15" :cursor "pointer" :fill "rgb(180,255,180)"} 1))
               
               
               
               ) 

      )
     )
   )



(defn render-aufgabeP2 [id action clip state this] 
 (let []
  (dom/div 
      (dom/h2 "Aufgabe " (str id))

      (dom/p "Ermitteln Sie rechnerisch, für welche Belegungen von x es Dreiecke A" 
              (dom/sub "n") "B" (dom/sub "n") "C" (dom/sub "n") " gibt.") 
             
      ;; click on aufg-specific tool (equals)
      ;; meta-select (von intro), select beide funktionen
      ;; auflosen

             
  )
 )
)


(defn render-aufgabeP3 [id action clip state this] 
 (let []
  (dom/div 
      (dom/h2 "Aufgabe " (str id))

      (dom/p "Zeigen Sie durch Rechnung, dass für die Länge der Strecken " (dom/span  {:style {:borderTop "1px solid black"}} "A" (dom/sub "n") "C" (dom/sub "n")) " in Abhängigkeit von x gilt: "
             "|" (dom/span {:style {:borderTop "1px solid black"}} "A" (dom/sub "n") "C" (dom/sub "n")) "|(x) = (-0,25x²+1,8x+4) LE."
              ) 
      
      ;; click on aufg-specific tool (minus)
      ;; pythagoras? nein
      ;; meta-select, A minus C
      ;; vereinfachen
      ;; verkehrt herum?

             
  )
 )
)

(defn render-aufgabeP4 [id action clip state this] 
 (let []
  (dom/div 
      (dom/h2 "Aufgabe " (str id))

      (dom/p "Ermitteln Sie die maximale Streckenlänge |"  (dom/span  {:style {:borderTop "1px solid black"}} "A" (dom/sub "0") "C" (dom/sub "0"))
      "| sowie den zugehörigen Wert für x. " (dom/br)
      "Berechnen Sie sodann den maximalen Flächeninhalt A" 
      (dom/sub "max") " der Dreiecke A" (dom/sub "n") "B" (dom/sub "n") "C" (dom/sub "n") "."
      
      ) 
             
    ;; meta-select (von aufgabe vorher)
    ;; Scheitelpunkt -> x und y
    ;; formelsammlung fur Flache gleichseitiges dreieck

             
  )
 )
)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

         ;;;;;;
        ;;;;;;;;
       ;;;;  ;;;;
      ;;;;    ;;;;
     ;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;
   ;;;;          ;;;;
  ;;;;            ;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn draw-triangle [xa ya xb yb xc yc]
 (let [dreieck-path (str "M" xa "," ya "L" xb "," yb "L" xc "," yc "L" xa "," ya "Z")
       schwerpunkt [(/ (+ xa xb xc) 3) (/ (+ ya yb yc) 3)] 
       font-size 30
       normieren (fn [x y] (let [length (Math/sqrt (+ (* x x) (* y y)))] [(/ x length) (/ y length)]))
       ecken-label (fn [x y offset] {:x (- (+ x (* offset (- x (first schwerpunkt)))) (/ font-size 2)) 
                                     :y (+ (+ y (* offset (- y (second schwerpunkt) ))) (/ font-size 2))})
       
       
       ;;; I AM DUMB!! I CAN JUST CLIP IT!!!!
       winkel (fn [x1 y1 x2 y2 x3 y3]  
              (let [richtung1 (map #(* % font-size 1.5) (normieren (- x2 x1) (- y2 y1)))
                    richtung2 (map #(* % font-size 1.5) (normieren (- x3 x1) (- y3 y1)))
                    x-start (+ x1 (first richtung1))
                    y-start (+ y1 (second richtung1))
                    x-end (+ x1 (first richtung2))
                    y-end (+ y1 (second richtung2))
                    bezier-punkt1 [(+ x-start (* 0.4 (first richtung2)) ) 
                                   (+ y-start (* 0.4 (second richtung2)) )]
                    bezier-punkt2 [(+ x-end (* 0.4 (first richtung1)) ) 
                                   (+ y-end (* 0.4 (second richtung1)) )]
       
       ] 
                                        (str "M" x-start "," y-start "C" 
                                        (first bezier-punkt1) "," (second bezier-punkt1)  " "
                                        (first bezier-punkt2) "," (second bezier-punkt2) " "
                                                            x-end "," y-end
                                          "L" x1 "," y1 "L" x-start "," y-start)))
        
       ]
  (dom/svg {:width "600" :height "300" :xmlns "http://www.w3.org/2000/svg"}
   ;; Dreieck
   (dom/path {:d dreieck-path :fill "none" :stroke "white" :strokeWidth "2px"}) 
   ;; Winkel
   (dom/path {:d (winkel xa ya xb yb xc yc) :fill "red" :stroke "white" :strokeWidth "2px"}) 
   (dom/path {:d (winkel xb yb xa ya xc yc) :fill "green" :stroke "white" :strokeWidth "2px"}) 
   (dom/path {:d (winkel xc yc xb yb xa ya) :fill "none" :stroke "white" :strokeWidth "2px"}) 
   ;; Ecken
   (dom/text  (merge {:fill "white" :fontSize font-size} (ecken-label xa ya 0.4)) "A")
   (dom/text  (merge {:fill "white" :fontSize (str font-size "px")} (ecken-label xb yb 0.4)) "B")
   (dom/text  (merge {:fill "white" :fontSize (str font-size "px")} (ecken-label xc yc 0.4)) "C")
   ;(dom/circle {:cx (first schwerpunkt) :cy (second schwerpunkt) :r 8 :fill "white"})



  )))


(defn render-dreieck1 [id action clip state this]
  (let [color "white"] 
   (dom/div
    (dom/h2 "Aufgabe " (str id))
    (draw-triangle 200 100 200 250 400 250)
    (dom/p {:style {:color "white"}} "Step 1: add to 180?")
    (dom/p {:style {:color "white"}} "Step 2: S+W pair?")
    (dom/p {:style {:color "white"}} "Step 1: SS or CS --vs-- definitely CS?")

    (dom/svg {:width "600" :height "300" :xmlns "http://www.w3.org/2000/svg"}
         (dom/g {:transform "translate(5 0)"}
                (dom/path {:d "M44.608,9.92C44.608,9.92 50.752,12.914 53.204,20.181C57.641,13.941 64.826,6.719 69.704,5.483" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x      
         (dom/g {:transform "translate(5 0)"}
                (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))
              ;x
         (dom/g {:transform "translate(5 45)"}
                (dom/path {:d "M96.126,8.179C96.126,8.179 104.541,12.759 110.911,20.974" :fill "none" :stroke color :strokeWidth "2px"})
                (dom/path {:d "M111.207,8.2C107.572,9.981 99.43,16.033 96.341,21.249" :fill "none" :stroke color :strokeWidth "2px"}))

         (dom/text {:x "50" :y "65" :fill color} "S+W")
         (dom/text {:x "50" :y "95" :fill color} "S+W/S")
         (dom/text {:x "100" :y "95" :fill color} "KoSa")


         (dom/path {:d "M65,30 L65,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M110,30 L110,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M100,30 L75,45" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M65,70 L65,80" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/path {:d "M110,70 L110,80" :fill "none" :stroke color :strokeWidth "2px"})


        ;triangle, two lupen      
         (dom/path {:d "M4.278,2.155L4.278,28.244L22.381,15.022L4.278,2.155Z" :fill "none" :stroke color :strokeWidth "2px"})
         (dom/g {:transform "translate(0 15)"}
                (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))
         (dom/g {:transform "translate(0 50)"}
                (dom/path {:d "M5.875,51.493C5.875,51.493 12.402,42.698 12.442,42.62C7.29,38.571 13.077,30.749 18.512,34.314C24.07,37.96 18.533,46.959 12.442,42.62" :fill "none" :stroke color :strokeWidth "2px"}))



         ;(dom/rect {:x "0" :y "0" :width "140" :height "110" :rx "5px" :ry "5px" :fill "rgb(223,239,255)" :fillOpacity "20%" :stroke "none" :cursor "pointer"})
          )
  
  
  )))


;; Dreieck-dummies ??


;; Rotation


;; Pyramide





#_(defn render-aufgabePX [id action clip state this] 
 (let []
 (dom/div 
      (dom/h2 "Aufgabe " (str id))

      (dom/p "Die Parabel p verläuft durch die Punkte" (dom/span " P(6|6) ")
             "und" (dom/span " Q(8|3) ")
             ". Sie hat die Gleichung der Form" (dom/span " y=-0,25x²+bx+c ")
             "(b, c, x, y \u220A IR).")
             
             
             )
 
 )

)


















(def aufgaben-renderer {:typP1 render-aufgabeP1})

(defn aufgabenlayout [aufgabe actions]
  (div {:style {:display "flex" :justifyContent "space-between" }
        };:minWidth "600px"}} 
       #_(div :ui.row)
       aufgabe (div ;{:style {:maxWidth "200px"}} 
                    actions) 
       ))

(defsc Aufgabe [this {:aufgabe/keys [id state actions clip typ] :as props}]
  {:query         [:aufgabe/id :aufgabe/typ :aufgabe/state :aufgabe/clip {:aufgabe/actions (comp/get-query AufgabenActions)}]
   :initial-state (fn [{:keys [id typ]}] 
                  {:aufgabe/id id
                   :aufgabe/typ typ
                   :aufgabe/state  #{} ;#{:p1 :p2 :eq :i1 :i2}
                   :aufgabe/actions [:aufg-actions/id id]
                   :aufgabe/clip false})
   :ident         :aufgabe/id} 
  

    
    (aufgabenlayout

     ((get aufgaben-renderer typ) id (:aufg-actions/active actions) clip state this)

      ;(p (str typ id ))  (p (str state))  (p (str actions))  (p "clip: " (str (if clip clip "empty")))

     (ui-aufg-actions actions))
   
)

(def ui-aufgabe (comp/factory Aufgabe {:keyfn :aufgabe/id}))




















#_(defmutation aufgabe1-statestuff [{:aufgabe/keys [newstate id]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:aufgabe/id id :aufgabe/state] newstate)) 
  )
#_(defmutation aufgabenactionsucksbigtime [{:aufg-actions/keys [id]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:aufg-actions/id id :aufg-actions/toolbox] ["fuck" "that" "shit"]))
  )




(defmutation aufgabe-nextstate [{:aufgabe/keys [id] :keys [content]}]
  (action [{:keys [state]}]          ;;;; multiple states: need to be list, NOT set or vec!!!
          (swap! state update-in [:aufgabe/id id :aufgabe/state] (if (seq? content) #(apply conj % content)  #(conj % content)))) 
  )

(defmutation aufgabe-clip [{:aufgabe/keys [id] :keys [content]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:aufgabe/id id :aufgabe/clip] content))
  )

(defmutation change-active-action [{:aufg-actions/keys [active id]}]
  (action [{:keys [state]}]
          (swap! state assoc-in [:aufg-actions/id id :aufg-actions/active] active))
  )

