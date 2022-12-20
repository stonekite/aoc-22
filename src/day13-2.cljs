(require '[clojure.string :as str])
(require '[cljs.reader :as reader])

(def fs (js/require "fs"))

(def dividers '([[2]] [[6]]))

(def lines
  (concat dividers
          (->> (str/split (.readFileSync fs "./inputs/day13.txt" "utf8") "\n")
               (filter #(not= "" %))
               (map
                reader/read-string))))

(defn compare-lines [a b]
  (let [pair [a b]]
    (if (every? number? pair)
      (if (apply < pair)
        -1
        (if (apply > pair)
          1
          0))
      (if (every? vector? pair)
        (reduce
         (fn [_ i]
           (let [[left right] (map #(get % i) pair)]
             (if (nil? left)
               (reduced -1)
               (if (nil? right)
                 (reduced 1)
                 (let [sub-res (compare-lines left right)]
                   (if (= 0 sub-res)
                     0
                     (reduced sub-res)))))))
         0
         (range (last (sort (map count pair)))))
        (apply compare-lines (map #(if (number? %) [%] %) pair))))))

(let [sorted-pairs (sort compare-lines lines)]
  (apply * (map #(+ 1 (.indexOf sorted-pairs %)) dividers)))
