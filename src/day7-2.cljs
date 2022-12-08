(require '[clojure.string :as str])

(def fs (js/require "fs"))

(def lines (rest (str/split (.readFileSync fs "./inputs/day7.txt" "utf8") "\n")))

(defn sum-tree [tree]
  (apply +
         (map
          (fn [v]
            (if (number? v) v (sum-tree v)))
          (vals tree))))

(let [tree (:tree
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
             lines))
      bound (- 30000000 (- 70000000 (sum-tree tree)))
      get-key-seqs (fn get-key-seqs [k-seq]
                     (let [v (get-in tree k-seq)]
                       (if
                        (number? v)
                         []
                         (conj
                          (apply concat (filter
                                         (fn [k-seq]
                                           (< 0 (count k-seq)))
                                         (map
                                          (fn [k]
                                            (get-key-seqs (vec (conj k-seq k))))
                                          (keys v))))
                          k-seq))))]
  (first
   (sort
    (filter
     (fn [size] (< bound size))
     (reduce
      (fn [a k]
        (concat a
                (map (fn [k-seq]
                       (sum-tree (get-in tree k-seq)))
                     (get-key-seqs [k]))))
      []
      (keys tree))))))
