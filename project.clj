(defproject corenlpd "0.4.1"
  :description "Stanford CoreNLP HTTP Server"
  :url "http://github.com/lucian303/corenlpd"

  :dependencies [[org.clojure/clojure "1.7.0-beta3"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.66"]
                 [environ "1.0.0"]
                 [compojure "1.3.4"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.1"]
                 [bouncer "0.3.2"]
                 [prone "0.8.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [ring-server "0.4.0"]
                 [metosin/compojure-api "0.20.1"]
                 [metosin/ring-swagger-ui "2.1.1-M2"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2"]
                 [edu.stanford.nlp/stanford-corenlp "3.5.2" :classifier "models"]
                 [clj-json "0.5.3"]]

  :min-lein-version "2.0.0"
  :uberjar-name "corenlpd.jar"
  :jvm-opts ["-server"]

  ;; Start the nREPL server when the application launches
  :env {:repl-port 7001}

  :main corenlpd.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [lein-beanstalk "0.2.7"]]

  :ring {:handler corenlpd.handler/app
         :init    corenlpd.handler/init
         :destroy corenlpd.handler/destroy
         :uberwar-name "corenlpd.war"
         :auto-reload? true
         :auto-refresh? true
         :port 8080
         :reload-paths ["src"]
         :stacktraces? true
         :open-browser? false}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         :repl-options {:init-ns corenlpd.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}

  :aws {:beanstalk
        {:environments [{:name "corenlpd-dev"}
                        {:name "corenlpd-prod"}]
         :region "us-west-1"
         :s3-bucket "corenlpd"}})
