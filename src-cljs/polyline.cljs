(ns polyline
  (:require [cljs.core.async :refer [chan]]
            [domina :refer [by-id]]
            [domina.events :refer [listen!]])
  (:require-macros [cljs.core.async.macros :as async :refer [go >! <!]]))

(defn draw-linestrip [linestrip context]
  (.beginPath context)
  (when-let [start (first linestrip)]
    (.moveTo context (first start) (second start))
    (dorun (map #(.lineTo context (first %) (second %)) (rest linestrip))))
  (.stroke context))

(defn add-coordinates [m evt bounds]
  (assoc m
    :x (- (:clientX evt) (.-left bounds))
    :y (- (:clientY evt) (.-top bounds))))

(defn ^:export start []
  (let [canvas (by-id "canvas")
        context (.getContext canvas "2d")
        bounds (.getBoundingClientRect canvas)
        events (chan)]
    (listen! canvas :mousedown (fn [evt]
                                 (go (>! events (add-coordinates {:type :mousedown} evt bounds)))))
    (listen! canvas :mousemove (fn [evt]
                                 (go (>! events (add-coordinates {:type :mousemove} evt bounds)))))
    (go (loop [linestrip []]
          (when-let [e (<! events)]
            (condp = (:type e)
              :mousemove (when-let [lastpoint (peek linestrip)]
                           (.clearRect context 0 0 500 500)
                           (draw-linestrip linestrip context)
                           (draw-linestrip [lastpoint [(:x e) (:y e)]] context))
              :mousedown (let [new-linestrip (conj linestrip [(:x e) (:y e)])]
                           (.clearRect context 0 0 500 500)
                           (draw-linestrip new-linestrip context)
                           (recur new-linestrip))
              nil)
            (recur linestrip))))))
