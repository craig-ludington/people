(defproject people "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [ring-middleware-format "0.5.0"]
                 [org.clojure/data.csv "0.1.2"]
                 [clj-time "0.10.0"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.cli "0.3.2"]]
  :plugins [[lein-ring "0.8.13"]
            [lein-bin "0.3.4"]]
  :main people.main
  :bin {:name "people"
        :bin-path "~/bin"
        :bootclasspath true}
  :ring {:handler people.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
