(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (rest (str/split (.readFileSync fs "./inputs/day7.txt" "utf8") "\n")))

(def counter (atom 0))

(defn sum-tree [tree]
  (let [sum (apply +
                   (map
                    (fn [v]
                      (if (number? v) v (sum-tree v)))
                    (vals tree)))]
    (if (< sum 100000) (do (swap! counter + sum) sum) sum)))

(do
  (reset! counter 0)
  (sum-tree
   (:tree
    (reduce
     (fn [a line]
       (let [line-parts (str/split line " ")]
         (case (first line-parts)
           "$" (case (second line-parts)
                 "cd" (if
                       (= ".." (last line-parts))
                        (update a :cur-path pop)
                        (update a :cur-path (fn [path]
                                              (conj path (keyword (last line-parts))))))
                 "ls" a)
           "dir" (update-in a (:cur-path a)
                            (fn [subtree]
                              (conj subtree [(keyword (second line-parts)) {}])))
           (update-in a (:cur-path a)
                      (fn [subtree]
                        (conj subtree [(keyword (second line-parts))
                                       (js/parseInt (first line-parts))]))))))
     {:cur-path [:tree]
      :tree {}}
     lines)))
  @counter)
