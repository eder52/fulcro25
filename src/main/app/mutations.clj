(ns app.mutations
  (:require 
    [com.wsscode.pathom.connect :as pc]))

(pc/defmutation spitti [env {:keys [state]}]
  {}
  (+ 2 3) ;(do (print "sure do") (spit "spitty.txt" (str state)))
  )


(def dummyshit 
  {:demoshit {:countershit {:counter 3}}})

(pc/defresolver spitit [env {:keys [statestuff]}]
  {::pc/input #{:statestuff}
   ::pc/output [:statestuff2]}
  ;(spit "finally.txt" (str statestuff))
  {:statestuff2 statestuff}
  )

(pc/defresolver spitit2 [env {}]
  {
   ::pc/output [:counter]}
  {:counter 4}
  )

(pc/defresolver loadingdemo [env {:demo/keys [id]}]
  {::pc/input #{:demo/id}
   ::pc/output [:demo/counter :demo/id :randomshit]}
  {:demo/counter 4 :demo/id id :randomshit "yo"}
  )

(pc/defresolver servertime [env {}]
  {::pc/output [:servertime :something]}
  {:servertime (java.util.Date.) :something "nix"
   })

(pc/defresolver saveshit [env {:keys [content]}]
  {::pc/input #{:content}
   ::pc/output [:saveshit]}
  (spit "zzz.txt" content)
  {:saveshit content
   })
(pc/defresolver retrieveshit [env {}]
  {::pc/output [:retrieveshit]}
  {:retrieveshit (slurp "zzz.txt")
   })

(pc/defresolver game-data-resolver [env {:keys [game-state]}]
  {::pc/input #{:game-state}
   ::pc/output [:game/data1 :game/data2]}
  {:game/data1 "haha" :game/data2 "nothing to see"})

(def resolvers [spitti spitit spitit2 loadingdemo servertime saveshit retrieveshit game-data-resolver])