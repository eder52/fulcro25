(ns app.model.session
  (:require
    [app.model.mock-database :as db]
    [datascript.core :as d]
    [com.fulcrologic.guardrails.core :refer [>defn => | ?]]
    [com.wsscode.pathom.connect :as pc :refer [defresolver defmutation]]
    [taoensso.timbre :as log]
    [clojure.spec.alpha :as s]
    [com.fulcrologic.fulcro.server.api-middleware :as fmw]))

(defonce account-database (atom {"test1@test" {:email "test1@test" :password "1234" :game/id 1}}))

;;;;;;;;;;;;;    this is how to read from txt file     ;;;;;;;;;;;
;(let [m (eval (read-string (slurp "./TESTSTUFF.txt")))]
;  (log/info (str "READING WORKS:   " (+ (:one m) (:two m)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defresolver current-session-resolver [env input]
  {::pc/output [{::current-session [:session/valid? :account/name]}]}   
  (let [{:keys [account/name session/valid?]} (get-in env [:ring/request :session])]   
    (if valid?
      (do
        (log/info name "already logged in!")
        
        {::current-session {:session/valid? true :account/name name}})   
      {::current-session {:session/valid? false}})))


;;;; DOES NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
(defresolver game-id-resolver [env _]
  {::pc/output [:game/id]}
  (let [{:keys [account/name session/valid?]} (get-in env [:ring/request :session])]
    (if valid?
      {:game/id (get (get @account-database name) :game/id)}
      {})
    ))
(defresolver game-resolver [env {:game/keys [id]}]
  {::pc/input #{:game/id}
   ::pc/output [:game/id :game/statestuff]}
      {:game/id id :game/statestuff "nothing to see here"}
    )
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn response-updating-session
  "Uses `mutation-response` as the actual return value for a mutation, but also stores the data into the (cookie-based) session."
  [mutation-env mutation-response]
  (let [existing-session (some-> mutation-env :ring/request :session)]
    (fmw/augment-response
      mutation-response
      (fn [resp]
        (let [new-session (merge existing-session mutation-response)]
          (assoc resp :session new-session))))))

(defmutation login [env {:keys [username password]}]
  {::pc/output [:session/valid? :account/name]}
  (log/info "Authenticating" username)
  (let [{expected-email    :email
         expected-password :password} (get @account-database username)]
    (if (and (= username expected-email) (= password expected-password))
      (response-updating-session env
        {:session/valid? true
         :account/name   username})
      (do
        (log/error "Invalid credentials supplied for" username)
        (throw (ex-info "Invalid credentials" {:username username}))))))

(defmutation logout [env params]
  {::pc/output [:session/valid?]}
  (response-updating-session env {:session/valid? false :account/name ""}))

(defmutation signup! [env {:keys [email password]}]
  {::pc/output [:signup/result]}
  (swap! account-database assoc email {:email    email
                                       :password password})
  {:signup/result "OK"})

(def resolvers [current-session-resolver    game-id-resolver game-resolver   login logout signup!])
