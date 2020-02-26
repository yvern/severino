(ns severino.core (:gen-class)
  (:require [aleph.http :refer [start-server]]
            [clojure.repl :refer [doc]]
            [clojure.core.async :refer [go <!! timeout]]
            [clojure.string :refer [split]]
            [jsonista.core :refer [write-value-as-string object-mapper read-value]
                           :rename {write-value-as-string jstr read-value jread}]))

(defn handler [conns maxconns dur chances req]
  (-> req (update :body jread) (jstr (object-mapper {:pretty true})) (println \newline))
  (if (<= maxconns @conns)
    {:status 503
     :headers {"content-type" "application/json" "Retry-After:" dur}
     :body (jstr {:error "limit reached" :connections @conns})}
    (do (swap! conns inc)
        (<!! (timeout dur))
        (go (swap! conns dec))
     {:status (rand-nth chances)
      :headers {"content-type" "application/json"}
      :body (jstr {:connections @conns})})))

(defn -main
  "Severino, an http client testing tool. You can use it to check:
  * your client can handle random errors (status codes)
  * your client's outgoing request (gets printed on the server terminal)
  * how many requests your client makes in a time window, to se if your throttling is working
  * how your client reacts to responses taking too long
  Usage:
    java -jar severino.jar port max-connetions request-duration [status-code=chance]+
  or, concretely:
    java -jar severino.jar 8080 10 10000 403=1 201=3
  Args:
  * port (int): desired port to serve
  * max-connections (int): will handle at most this number of connections, responding with 503 immediatly if this limit is reached
  * duration (int): amout of time, in ms, to keep client waiting before sending response
  * chances (key value int pairs): status codes and their relative chances of occuring, for example '403=1 201=3' means 1 in every 4 responses will have status code 403, and the remaining 3 will have 201
  "
  ([] (doc -main))
  ([p maxconns dur & chances]
   (let [conns (atom 0)]
     (println (str "listening on port " p))
     (println (str "handling at most " maxconns " at a time, with " dur " delay"))
     (println (str "codes and chances: " chances))
     (start-server
       (partial handler conns
         (Integer/parseInt maxconns)
         (Integer/parseInt dur)
         (into [] (comp
           (mapcat #(split % #"="))
           (map #(Integer/parseInt %))
           (partition-all 2)
           (mapcat (fn [[s c]] (repeat c s))))
           chances))
       {:port (Integer/parseInt p)}))))
