(ns todo-app.core
  (:require [compojure.core :refer [routes]]
            [ring.adapter.jetty :as server]
            [todo-app.handler.main :refer [main-routes]]
            [todo-app.handler.todo :refer [todo-routes]]
            [todo-app.middleware :refer [wrap-dev]]))

(defonce server (atom nil))

(def app
  (->
    (routes
      todo-routes
      main-routes)
    wrap-dev))

(defn start-server []
  (when-not @server
    (reset! server (server/run-jetty #'app {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))

