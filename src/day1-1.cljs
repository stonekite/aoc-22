(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def blocks (str/split (.readFileSync fs "./inputs/day1.txt" "utf8") "\n\n"))

(def sumblock (memoize (fn [block]
                         (reduce (fn [a b] (+ a (#(js/parseInt %) b)))
                                 0
                                 (str/split block #"\n")))))

(reduce (fn [a block]
          (if (> (sumblock a) (sumblock block))
            (sumblock a)
            (sumblock block)))
        blocks)
