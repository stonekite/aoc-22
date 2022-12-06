(def fs (js/require "fs"))

(def buffer (.readFileSync fs "./inputs/day6.txt" "utf8"))

(reduce
 (fn [a char]
   (let [new-a (conj (if (> (count a) 3) (subvec a 1) a) char)]
     (if
      (and
       (= 4 (count new-a))
       (= (count (set new-a)) (count new-a)))
       (reduced (+ 4 (.indexOf buffer (apply + new-a))))
       new-a)))
 []
 buffer)
