(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (str/split (.readFileSync fs "./inputs/day2.txt" "utf8") "\n"))

(def rules {:A 1 :B 2 :C 3})

(def plays {
            :X #(+ 0 (case % 1 3 2 1 3 2))
            :Y #(+ 3 (case % 1 1 2 2 3 3))
            :Z #(+ 6 (case % 1 2 2 3 3 1))})

(defn solve-line [line]
  (let [[opponent outcome] (str/split line " ")] 
    ((get plays (keyword outcome)) (get rules (keyword opponent)))))

(reduce (fn [a line] (+ a (solve-line line))) 0 lines)
