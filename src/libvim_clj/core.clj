(ns libvim-clj.core
  (:require [libvim-clj.constants :as constants])
  (:import [org.lwjgl.system Library SharedLibrary CallbackI$V MemoryUtil Platform]
           [org.lwjgl.system.dyncall DynCall DynCallback]))

(defn- ptr->str [^long ptr]
  (when (pos? ptr)
    (MemoryUtil/memUTF8 ptr)))

(defprotocol IVim
  (init [vim])
  (open-buffer [vim file-name])
  (get-current-buffer [vim])
  (set-current-buffer [vim buffer-ptr])
  (get-file-name [vim buffer-ptr])
  (get-line [vim buffer-ptr line-num])
  (get-line-count [vim buffer-ptr])
  (set-on-buffer-update [vim callback])
  (set-on-auto-command [vim callback])
  (get-command-text [vim])
  (get-command-position [vim])
  (get-command-completion [vim])
  (get-cursor-column [vim])
  (get-cursor-line [vim])
  (set-cursor-position [vim line-num col-num])
  (input [vim input])
  (execute [vim cmd])
  (set-on-quit [vim callback])
  (set-tab-size [vim size])
  (get-tab-size [vim])
  (get-visual-type [vim])
  (visual-active? [vim])
  (select-active? [vim])
  (get-visual-range [vim])
  (get-window-width [vim])
  (get-window-height [vim])
  (get-window-top-line [vim])
  (get-window-left-column [vim])
  (set-window-width [vim width])
  (set-window-height [vim height])
  (set-window-top-left [vim top left])
  (get-mode [vim]))

(defn ->vim
  "Returns an object that you can call the other functions on.
  You *must* call the `init` function on it before anything else.
  Line numbers are 1-based and column numbers are 0-based (don't ask me why)."
  []
  (let [libname (condp = (Platform/get)
                  Platform/WINDOWS "libvim"
                  Platform/MACOSX "vim"
                  Platform/LINUX "vim"
                  (throw (ex-info "Can't find a vim binary for your platform" {})))
        ^SharedLibrary lib (try
                             (Library/loadNative nil libname)
                             (catch NullPointerException _
                               (throw (ex-info "LWJGL 3.2.3 or greater is required" {}))))
        init* (.getFunctionAddress lib "vimInit")
        open-buffer* (.getFunctionAddress lib "vimBufferOpen")
        get-current-buffer* (.getFunctionAddress lib "vimBufferGetCurrent")
        set-current-buffer* (.getFunctionAddress lib "vimBufferSetCurrent")
        get-file-name* (.getFunctionAddress lib "vimBufferGetFilename")
        get-line* (.getFunctionAddress lib "vimBufferGetLine")
        get-line-count* (.getFunctionAddress lib "vimBufferGetLineCount")
        set-on-buffer-update* (.getFunctionAddress lib "vimSetDestructuredBufferUpdateCallback")
        set-on-auto-command* (.getFunctionAddress lib "vimSetAutoCommandCallback")
        get-command-text* (.getFunctionAddress lib "vimCommandLineGetText")
        get-command-position* (.getFunctionAddress lib "vimCommandLineGetPosition")
        get-command-completion* (.getFunctionAddress lib "vimCommandLineGetCompletion")
        get-cursor-column* (.getFunctionAddress lib "vimCursorGetColumn")
        get-cursor-line* (.getFunctionAddress lib "vimCursorGetLine")
        set-cursor-position* (.getFunctionAddress lib "vimCursorSetPositionDestructured")
        input* (.getFunctionAddress lib "vimInput")
        execute* (.getFunctionAddress lib "vimExecute")
        set-on-quit* (.getFunctionAddress lib "vimSetQuitCallback")
        set-tab-size* (.getFunctionAddress lib "vimOptionSetTabSize")
        get-tab-size* (.getFunctionAddress lib "vimOptionGetTabSize")
        get-visual-type* (.getFunctionAddress lib "vimVisualGetType")
        visual-active?* (.getFunctionAddress lib "vimVisualIsActive")
        select-active?* (.getFunctionAddress lib "vimSelectIsActive")
        get-visual-start-line* (.getFunctionAddress lib "vimVisualGetStartLine")
        get-visual-start-column* (.getFunctionAddress lib "vimVisualGetStartColumn")
        get-visual-end-line* (.getFunctionAddress lib "vimVisualGetEndLine")
        get-visual-end-column* (.getFunctionAddress lib "vimVisualGetEndColumn")
        get-window-width* (.getFunctionAddress lib "vimWindowGetWidth")
        get-window-height* (.getFunctionAddress lib "vimWindowGetHeight")
        get-window-top-line* (.getFunctionAddress lib "vimWindowGetTopLine")
        get-window-left-column* (.getFunctionAddress lib "vimWindowGetLeftColumn")
        set-window-width* (.getFunctionAddress lib "vimWindowSetWidth")
        set-window-height* (.getFunctionAddress lib "vimWindowSetHeight")
        set-window-top-left* (.getFunctionAddress lib "vimWindowSetTopLeft")
        get-mode* (.getFunctionAddress lib "vimGetMode")
        vm (DynCall/dcNewCallVM 1024)]
    (reify IVim
      (init [this]
        (DynCall/dcMode vm DynCall/DC_CALL_C_DEFAULT)
        (DynCall/dcReset vm)
        (DynCall/dcCallVoid vm init*))
      (open-buffer [this file-name]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (-> ^CharSequence file-name MemoryUtil/memUTF8 MemoryUtil/memAddress))
        (DynCall/dcArgLong vm 1)
        (DynCall/dcArgInt vm 0)
        (DynCall/dcCallPointer vm open-buffer*))
      (get-current-buffer [this]
        (DynCall/dcReset vm)
        (DynCall/dcCallPointer vm get-current-buffer*))
      (set-current-buffer [this buffer-ptr]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm buffer-ptr)
        (DynCall/dcCallVoid vm set-current-buffer*))
      (get-file-name [this buffer-ptr]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm buffer-ptr)
        (ptr->str (DynCall/dcCallPointer vm get-file-name*)))
      (get-line [this buffer-ptr line-num]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm buffer-ptr)
        (DynCall/dcArgLong vm line-num)
        (ptr->str (DynCall/dcCallPointer vm get-line*)))
      (get-line-count [this buffer-ptr]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm buffer-ptr)
        (DynCall/dcCallLong vm get-line-count*))
      (set-on-buffer-update [this callback]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (MemoryUtil/memAddressSafe
                                   (reify CallbackI$V
                                     (callback [this args]
                                       (let [buffer-ptr (DynCallback/dcbArgPointer args)
                                             start-line (DynCallback/dcbArgLong args)
                                             end-line (DynCallback/dcbArgLong args)
                                             line-count (DynCallback/dcbArgLong args)]
                                         (callback buffer-ptr start-line end-line line-count)))
                                     (getSignature [this]
                                       "(plll)v"))))
        (DynCall/dcCallVoid vm set-on-buffer-update*))
      (set-on-auto-command [this callback]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (MemoryUtil/memAddressSafe
                                   (reify CallbackI$V
                                     (callback [this args]
                                       (let [event (DynCallback/dcbArgInt args)
                                             buffer-ptr (DynCallback/dcbArgPointer args)]
                                         (callback buffer-ptr (constants/auto-events event))))
                                     (getSignature [this]
                                       "(ip)v"))))
        (DynCall/dcCallVoid vm set-on-auto-command*))
      (get-command-text [this]
        (DynCall/dcReset vm)
        (ptr->str (DynCall/dcCallPointer vm get-command-text*)))
      (get-command-position [this]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-command-position*))
      (get-command-completion [this]
        (DynCall/dcReset vm)
        (ptr->str (DynCall/dcCallPointer vm get-command-completion*)))
      (get-cursor-column [this]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-cursor-column*))
      (get-cursor-line [this]
        (DynCall/dcReset vm)
        (DynCall/dcCallLong vm get-cursor-line*))
      (set-cursor-position [this line-num col-num]
        (DynCall/dcReset vm)
        (DynCall/dcArgLong vm line-num)
        (DynCall/dcArgInt vm col-num)
        (DynCall/dcCallLong vm set-cursor-position*))
      (input [this input]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (-> ^CharSequence input MemoryUtil/memUTF8 MemoryUtil/memAddress))
        (DynCall/dcCallVoid vm input*))
      (execute [this cmd]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (-> ^CharSequence cmd MemoryUtil/memUTF8 MemoryUtil/memAddress))
        (DynCall/dcCallVoid vm execute*))
      (set-on-quit [this callback]
        (DynCall/dcReset vm)
        (DynCall/dcArgPointer vm (MemoryUtil/memAddressSafe
                                   (reify CallbackI$V
                                     (callback [this args]
                                       (let [buffer-ptr (DynCallback/dcbArgPointer args)
                                             force? (DynCallback/dcbArgBool args)]
                                         (callback buffer-ptr force?)))
                                     (getSignature [this]
                                       "(pb)v"))))
        (DynCall/dcCallVoid vm set-on-quit*))
      (set-tab-size [this size]
        (DynCall/dcReset vm)
        (DynCall/dcArgInt vm size)
        (DynCall/dcCallVoid vm set-tab-size*))
      (get-tab-size [this]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-tab-size*))
      (get-visual-type [this]
        (DynCall/dcReset vm)
        (char (DynCall/dcCallInt vm get-visual-type*)))
      (visual-active? [this]
        (DynCall/dcReset vm)
        (= 1 (DynCall/dcCallInt vm visual-active?*)))
      (select-active? [this]
        (DynCall/dcReset vm)
        (= 1 (DynCall/dcCallInt vm select-active?*)))
      (get-visual-range [this]
        (let [_ (DynCall/dcReset vm)
              start-line (DynCall/dcCallLong vm get-visual-start-line*)
              _ (DynCall/dcReset vm)
              start-column (DynCall/dcCallInt vm get-visual-start-column*)
              _ (DynCall/dcReset vm)
              end-line (DynCall/dcCallLong vm get-visual-end-line*)
              _ (DynCall/dcReset vm)
              end-column (DynCall/dcCallInt vm get-visual-end-column*)]
          {:start-line start-line
           :start-column start-column
           :end-line end-line
           :end-column end-column}))
      (get-window-width [vim]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-window-width*))
      (get-window-height [vim]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-window-height*))
      (get-window-top-line [vim]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-window-top-line*))
      (get-window-left-column [vim]
        (DynCall/dcReset vm)
        (DynCall/dcCallInt vm get-window-left-column*))
      (set-window-width [vim width]
        (DynCall/dcReset vm)
        (DynCall/dcArgInt vm width)
        (DynCall/dcCallVoid vm set-window-width*))
      (set-window-height [vim height]
        (DynCall/dcReset vm)
        (DynCall/dcArgInt vm height)
        (DynCall/dcCallVoid vm set-window-height*))
      (set-window-top-left [vim top left]
        (DynCall/dcReset vm)
        (DynCall/dcArgInt vm top)
        (DynCall/dcArgInt vm left)
        (DynCall/dcCallVoid vm set-window-top-left*))
      (get-mode [this]
        (DynCall/dcReset vm)
        (constants/modes (DynCall/dcCallInt vm get-mode*))))))

