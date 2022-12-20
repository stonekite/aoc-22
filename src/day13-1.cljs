(require '[clojure.string :as str])
(require '[cljs.reader :as reader])

(def fs (js/require "fs"))

(def pairs
  (map
   (fn [block]
     (map
      reader/read-string
      (str/split block "\n")))
   (str/split (.readFileSync fs "./inputs/day13.txt" "utf8") "\n\n")))

(defn compare-lines [pair]
  (if (every? number? pair)
    (if (apply < pair)
      true
      (if (apply > pair)
        false
        nil))
    (if (every? vector? pair)
      (reduce
       (fn [a i]
         (let [[left right] (map #(get % i) pair)]
           (if (nil? left)
             (reduced true)
             (if (nil? right)
               (reduced false)
               (let [sub-res (compare-lines [left right])]
                 (if (nil? sub-res)
                   a
                   (reduced sub-res)))))))
       nil
       (range (last (sort (map count pair)))))
      (compare-lines (map #(if (number? %) [%] %) pair)))))

(reduce
 (fn [a [i v]]
   (if (compare-lines v) (+ a i 1) a))
 0
 (map-indexed (fn [i v] [i v]) pairs))
