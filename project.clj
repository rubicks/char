(defproject char "0.1.0-SNAPSHOT"
  :repl-options
  {:init-ns char.repl}
  :dependencies [
                 [cheshire              "5.3.1"     ]
                 [clj-http              "0.9.0"     ]
                 [com.h2database/h2     "1.3.175"   ]
                 [com.taoensso/timbre   "3.0.0"     ]
                 [com.taoensso/tower    "2.0.1"     ]
                 [compojure             "1.1.6"     ]
                 [environ               "0.4.0"     ]
                 [korma                 "0.3.0-RC6" ]
                 [lib-noir              "0.8.1"     ]
                 [liberator             "0.11.0"    ]
                 [markdown-clj          "0.9.41"    ]
                 [org.clojure/clojure   "1.5.1"     ]
                 [org.clojure/data.csv  "0.1.2"     ]
                 [org.clojure/data.json "0.2.4"     ]
                 [ring-server           "0.3.1"     ]
                 [selmer                "0.6.1"     ]
                 [log4j
                  "1.2.17"
                  :exclusions
                  [javax.mail/mail
                   javax.jms/jms
                   com.sun.jdmk/jmxtools
                   com.sun.jmx/jmxri]]
                 ]
  :ring
  {:handler char.handler/app,
   :init char.handler/init,
   :destroy char.handler/destroy}
  :profiles
  {:uberjar {:aot :all},
   :production {:ring {:open-browser? false,
                       :stacktraces? false,
                       :auto-reload? false}},
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]],
         :env {:dev true}}}
  :url "http://example.com/FIXME"
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]]
  :description "the mbta needs some help"
  :min-lein-version "2.3.4")
