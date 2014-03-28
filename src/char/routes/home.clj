;; char/src/char/routes/home.clj

(ns char.routes.home
  (:use compojure.core)
  (:require
   [char.get]
   [char.util         :as util]
   [char.views.layout :as layout]
   [cheshire.core]
   [clj-http.client   :as client]
   [clojure.data.csv  :as csv]
   [clojure.pprint    :as pp ]
   [compojure.core    :refer :all]
   [liberator.core    :refer [resource defresource]]
   ))


(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))


(defn about-page []
  (layout/render "about.html"))


(defresource foo
  :handle-ok "This is the foo page."
  :etag      "fixed-etag"
  :available-media-types ["text/plain"])


(defresource bar
  :service-available? true
  :allowed-methods [:get]
  :handle-ok (cheshire.core/generate-string {"foo" 42 "bar" 43} {:pretty true})
  :etag      "fixed-etag"
  :available-media-types ["application/json"])


(defn- -jpretty [o] (cheshire.core/generate-string o {:pretty true}))

(defn- -departures-txt []
  (let [ar (csv/read-csv (:body (char.get/departures)))]
    (let [ws (util/cwidths ar)]
      (util/stripose "\n"
                     (map #(util/stripose " " (map util/fornat %1 %2))
                          (repeat ws) ar)))))


(defresource departures
  :allowed-methods [:get]
  :handle-ok (-jpretty (char.get/departures))
  :etag "fixed-etag"
  :available-media-types ["application/json"]
  )
(defresource departures-txt
  :allowed-methods [:get]
  :handle-ok (-departures-txt)
  :etag "fixed-etag"
  :available-media-types ["text/plain"]
  )


(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (ANY "/foo" request foo)
  (ANY "/bar" request bar)
  (ANY "/departures" request departures)
  (ANY "/departures.txt" request departures-txt)
  )
