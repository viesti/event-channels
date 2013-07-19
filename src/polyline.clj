(ns polyline
  (:require [clojure.core.async :as async :refer [<! >! go chan close! sliding-buffer]])
  (:import (com.sun.javafx.application PlatformImpl)
           (javafx.application Platform)
           (javafx.event EventHandler Event)
           (javafx.stage Screen Stage WindowEvent)
           (javafx.scene Scene Group Node)
           (javafx.scene.input MouseEvent MouseButton)
           (javafx.scene.layout Pane)
           (javafx.scene.paint Color Paint)
           (javafx.scene.shape Rectangle Polyline Line)))

(defonce jfx (do (PlatformImpl/startup #(println "JavaFX started")) true))

(defn in-jfx [f] (if jfx (Platform/runLater f)))

(defn close-channels-on-close [window & cs]
  (let [old (.getOnCloseRequest window)
        new (proxy [EventHandler] []
              (handle [^WindowEvent event]
                (dorun (map #(close! %) cs))
                (.setValue (.onCloseRequestProperty window) old)))]
    (.setOnCloseRequest window new)))

(defn events->chan [src chan]
  (.addEventHandler src Event/ANY (proxy [EventHandler] []
                                    (handle [event]
                                      (go (>! chan event))))))

(defn in-pane [& nodes]
  (let [pane (Pane.)]
    (.addAll (.getChildren pane) nodes)
    pane))

(defn position-line [line x1 y1 x2 y2]
  (doto line
    (.setStartX x1)
    (.setStartY y1)
    (.setEndX x2)
    (.setEndY y2)))

(defn start []
  (let [polyline (Polyline.)
        line (Line.)
        events (chan)]
    (in-jfx #(doto (Stage.)
               (close-channels-on-close events)
               (.setScene (doto (Scene. (in-pane polyline line) 500.0 500.0 Color/ALICEBLUE)
                            (events->chan events)))
               (.centerOnScreen)
               (.show)
               (.toFront)))
    (go (loop [linestrip []]
          (when-let [v (<! events)]
            (condp = (.getEventType v)
              MouseEvent/MOUSE_MOVED (when-let [lastpoint (peek linestrip)]
                                       (in-jfx #(doto line
                                                  (.setVisible true)
                                                  (position-line (first lastpoint)
                                                                 (second lastpoint)
                                                                 (.getX v)
                                                                 (.getY v)))))
              MouseEvent/MOUSE_PRESSED (let [new-linestrip (conj linestrip [(.getX v) (.getY v)])]
                                         (.setVisible line false)
                                         (in-jfx #(.setAll (.getPoints polyline) (flatten new-linestrip)))
                                         (recur new-linestrip))
              nil)
            (recur linestrip))
          (println "Done listening")))))
