(ns libvim-clj.examples
  (:require [libvim-clj.core]
            [dynadoc.example :refer [defexample defexamples]]))

(defexample libvim-clj.core/open-buffer
  (open-buffer vim "hello.txt"))

(defexamples libvim-clj.core/input
  ["Input a character"
   (input vim "h")]
  ["Input a special character
   See: https://vim.fandom.com/wiki/Mapping_keys_in_Vim_-_Tutorial_%28Part_2%29"
   (input vim "<Enter>")])

(defexample libvim-clj.core/execute
  (execute vim "set expandtab"))

(defexample libvim-clj.core/set-on-quit
  (set-on-quit vim (fn [buffer-ptr forced?]
                     (System/exit 0))))

