(ns app.mutations
  (:require 
    [com.fulcrologic.fulcro.mutations :refer [defmutation]]))

(defmutation spitti [{:keys [state]}]
  ;{}
  (action [{:keys [state]}]
          (swap! state update-in [:demoshit :countershit :counter] inc))
  (remote [env] true))

