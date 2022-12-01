(require '[clojure.string :as str])

(def fs (js/require "fs"))

(defn sumblock [block]
  (reduce (fn [a b] (+ a (#(js/parseInt %) b))) 0 (str/split block #"\n")))

(let [blocks (str/split (.readFileSync fs "./inputs/day1.txt" "utf8") "\n\n")] 
  (reduce (fn [a b]  
            (if (> (sumblock a) (sumblock b)) 
              (sumblock a) (sumblock b))) blocks))