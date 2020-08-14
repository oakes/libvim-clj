(ns libvim-clj.examples
  (:require [libvim-clj.core]
            [dynadoc.example :refer [defexample defexamples]]))

(defexample libvim-clj.core/open-buffer
  (open-buffer vim "hello.txt"))

(defexample libvim-clj.core/set-on-buffer-update
  (set-on-buffer-update vim (fn [buffer-ptr start-line end-line line-count]
                              (println "Buffer" buffer-ptr "was updated"))))

(defexample libvim-clj.core/set-on-auto-command
  (set-on-auto-command vim (fn [buffer-ptr event]
                             (case event
                               EVENT_BUFENTER (println "User entered a buffer")
                               nil))))

(defexamples libvim-clj.core/input
  ["Input a character"
   (input vim "h")]
  ["Input a special character
   See: https://vim.fandom.com/wiki/Mapping_keys_in_Vim_-_Tutorial_%28Part_2%29"
   (input vim "<Enter>")])

(defexample libvim-clj.core/input-unicode
  "Input a unicode character"
  (input-unicode vim "者"))

(defexample libvim-clj.core/execute
  (execute vim "set expandtab"))

(defexample libvim-clj.core/set-on-quit
  (set-on-quit vim (fn [buffer-ptr forced?]
                     (System/exit 0))))

(defexample libvim-clj.core/set-on-unhandled-escape
  (set-on-unhandled-escape vim (fn []
                                 (println "User pressed Esc in normal mode"))))

(defexample libvim-clj.core/set-on-stop-search-highlight
  (set-on-stop-search-highlight vim (fn []
                                      (println ":noh was executed"))))

(defexample libvim-clj.core/set-on-yank
  (set-on-yank vim (fn [{:keys [start-line start-column end-line end-column]}]
                     (println "User copied text"))))

(defexample libvim-clj.core/set-on-message
  (set-on-message vim (fn [{:keys [title message message-priority]}]
                        (println message))))

