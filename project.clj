(defproject severino "0.1.0"
  :description "Severino, an http client testing tool."
  :url "https://github.com/yvendruscolo/severino"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [aleph "0.4.6"]
                 [metosin/jsonista "0.2.5"]
                 [org.clojure/core.async "1.0.567"]]
  :main severino.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
