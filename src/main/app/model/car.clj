(ns app.model.car
  (:require 
   ;[com.wsscode.pathom.connect :as pc]
  
   [app.model.mock-database :as db]
    
    
    [com.wsscode.pathom.connect :as pc :refer [defresolver defmutation]]
    [taoensso.timbre :as log])
    )

(def cars 
  {1 {:car/id 1
      :car/make "Honda"
      :car/model "Accord"}
   2 {:car/id 2
      :car/make "Ford"
      :car/model "F-150"}})

(pc/defresolver car-resolver [env {:keys [id]}]
  {::pc/input #{:car/id}
   ::pc/output [:car/id :car/make :car/model]}
  (get cars id))





(defresolver all-users-resolveris [{:keys [db]} input]
  {;;GIVEN nothing (e.g. this is usable as a root query)
   ;; I can output all accounts. NOTE: only ID is needed...other resolvers resolve the rest
   ::pc/output [{:all-accs [:account/id]}]}
  {:all-accs (mapv
                   (fn [id] {:account/id id})
                   [2 4 5])})





(def resolvers [car-resolver all-users-resolveris])



