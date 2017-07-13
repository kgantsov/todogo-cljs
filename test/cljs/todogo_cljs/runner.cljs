(ns todogo-cljs.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [todogo-cljs.core-test]))

(doo-tests 'todogo-cljs.core-test)
