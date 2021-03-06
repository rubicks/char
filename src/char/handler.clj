(ns char.handler
  (:require
   [char.middleware                 :as middleware]
   [char.routes.home                :refer [home-routes]]
   [compojure.core                  :refer [defroutes ANY]]
   [compojure.route                 :as route]
   [environ.core                    :refer [env]]
   [noir.util.middleware            :refer [app-handler]]
   [selmer.parser                   :as parser]
   [taoensso.timbre                 :as timbre]
   [taoensso.timbre.appenders.rotor :as rotor]
   )
  )


(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))


(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "char.log" :max-size (* 512 1024) :backlog 10})

  (if (env :dev) (parser/cache-off!))
  (timbre/info "char started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "char is shutting down..."))



(def app (noir.util.middleware/app-handler
           ;; add your application routes here
           [home-routes
            app-routes]
           ;; add custom middleware here
           :middleware [middleware/template-error-page
                        middleware/log-request]
           ;; add access rules here
           :access-rules []
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
