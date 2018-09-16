(ns todo-clj.core
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as server]
            [ring.util.response :as res]))

(defonce server (atom nil))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello, world!</h1>"})

(defn start-server []
  (when-not @server
    (reset! server (server/run-jetty #'handler {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))

;(require '[todo-clj.core :as c])
;(c/start-server)


(defn html [res]
  (assoc res :headers {"Content-Type" "text/html; charset=utf-8"}))

(defn ok [body]
  {:status 200
   :body body})

(defn home-view [req]
  "<h1>ホーム画面</h1>
   <a href=\"/todo\">TODO 一覧</a>")

(defn home [req]
  (-> (home-view req)
      ok
      html))

(def todo-list
  [{:title "朝ごはんを作る"}
   {:title "燃えるゴミを出す"}
   {:title "卵を買って帰る"}
   {:title "お風呂を洗う"}])

(defn todo-index-view [req]
  `("<h1>TODO 一覧</h1>"
     "<ul>"
     ~@(for [{:keys [title]} todo-list]
         (str "<li>" title "</li>"))
     "</ul>"))

(defn todo-index [req]
  (-> (todo-index-view req)
      ok
      html))

(defroutes handler
           (GET "/" req home)
           (GET "/todo" req todo-index)
           (route/not-found "<h1>404 page not found</h1>"))

(comment)
