(def fs (js/require "fs"))

(def buffer (.readFileSync fs "./inputs/day6.txt" "utf8"))

(reduce
 (fn [a char]
   (let [new-a (conj (if (> (count a) 13) (subvec a 1) a) char)]
     (if
      (and
       (= 14 (count new-a))
       (= (count (set new-a)) (count new-a)))
       (reduced (+ 14 (.indexOf buffer (apply + new-a))))
       new-a)))
 []
 buffer)
