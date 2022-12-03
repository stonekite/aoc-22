(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def blocks (str/split (.readFileSync fs "./inputs/day1.txt" "utf8") "\n\n"))

(def sumblock (memoize (fn [block]
  (reduce (fn [a b] (+ a (#(js/parseInt %) b))) 0 (str/split block #"\n")))))

(reduce + (reduce (fn [a b]
          (let [with-b (sort (cons (sumblock b) a))]
             (if (= 4 (count with-b)) (rest with-b) with-b))) [] blocks))
