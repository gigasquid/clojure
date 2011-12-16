;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

; Author: Carin Meier

(ns clojure.test-clojure.let
  (:use clojure.test))

(deftest let-basic-uses
  (testing "simple binding"
    (is (= 1
          (eval '(let [x 1 y x] x)))))

  (testing "let vector bindings"
    (is (= [1 2 3 '(4 5 6 7) [1 2 3 4 5 6 7]]
          (eval '(let [[a b c & d :as e] [1 2 3 4 5 6 7]]
                   [a b c d e])) )))

  (testing "let nested vector bindings"
    (is (= [1 2 3 4]
          (eval '(let [[[x1 y1][x2 y2]] [[1 2] [3 4]]]
                   [x1 y1 x2 y2])))))
  
  (testing "let string to vector binding"
    (is (= [\a \s '(\d \j \h \h \f \d \a \s) "asdjhhfdas"]
          (eval '(let [[a b & c :as str] "asdjhhfdas"]
                   [a b c str])))))

  (testing "let map bindings"
    (is (= [5 3 6 {:c 6, :a 5}]
          (eval '(let [{a :a, b :b, c :c, :as m :or {a 2 b 3}} {:a 5 :c 6}]
                   [a b c m])))))
  
  (testing "let nested map bindings"
    (is (= [12 15 16 22 23 '(24 25) [22 23 24 25]]
          (eval '(let [{j :j, k :k, i :i, [r s & t :as v] :ivec, :or {i 12 j 13}}
                        {:j 15 :k 16 :ivec [22 23 24 25]}]
                   [i j k r s t v]))))))


(deftest let-error-checking
 (testing "destructuring map to vector error"
    (is (fails-with-cause? java.lang.Exception
          #"let cannot destructure class clojure.lang.PersistentArrayMap. Try converting it to a seq."
          (eval '(let [[x y] {}] x)))))
  (testing "destructuring set to vector error"
    (is (fails-with-cause? java.lang.Exception
          #"let cannot destructure class clojure.lang.PersistentHashSet. Try converting it to a seq."
          (eval '(let [[x y] #{}] x)))))
  (testing "destructuring long to vector error"
    (is (fails-with-cause? java.lang.Exception
          #"let cannot destructure class java.lang.Long."
          (eval '(let [[x y] 1] x))))))
