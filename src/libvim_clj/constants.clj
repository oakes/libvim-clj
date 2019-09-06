(ns libvim-clj.constants)

(def auto-events
  '[EVENT_BUFADD,               ;; after adding a buffer to the buffer list
    EVENT_BUFDELETE,            ;; deleting a buffer from the buffer list
    EVENT_BUFENTER,             ;; after entering a buffer
    EVENT_BUFFILEPOST,          ;; after renaming a buffer
    EVENT_BUFFILEPRE,           ;; before renaming a buffer
    EVENT_BUFHIDDEN,            ;; just after buffer becomes hidden
    EVENT_BUFLEAVE,             ;; before leaving a buffer
    EVENT_BUFNEW,               ;; after creating any buffer
    EVENT_BUFNEWFILE,           ;; when creating a buffer for a new file
    EVENT_BUFREADCMD,           ;; read buffer using command
    EVENT_BUFREADPOST,          ;; after reading a buffer
    EVENT_BUFREADPRE,           ;; before reading a buffer
    EVENT_BUFUNLOAD,            ;; just before unloading a buffer
    EVENT_BUFWINENTER,          ;; after showing a buffer in a window
    EVENT_BUFWINLEAVE,          ;; just after buffer removed from window
    EVENT_BUFWIPEOUT,           ;; just before really deleting a buffer
    EVENT_BUFWRITECMD,          ;; write buffer using command
    EVENT_BUFWRITEPOST,         ;; after writing a buffer
    EVENT_BUFWRITEPRE,          ;; before writing a buffer
    EVENT_CMDLINECHANGED,       ;; command line was modified
    EVENT_CMDLINEENTER,         ;; after entering the command line
    EVENT_CMDLINELEAVE,         ;; before leaving the command line
    EVENT_CMDUNDEFINED,         ;; command undefined
    EVENT_CMDWINENTER,          ;; after entering the cmdline window
    EVENT_CMDWINLEAVE,          ;; before leaving the cmdline window
    EVENT_COLORSCHEME,          ;; after loading a colorscheme
    EVENT_COLORSCHEMEPRE,       ;; before loading a colorscheme
    EVENT_COMPLETECHANGED,      ;; after completion popup menu changed
    EVENT_COMPLETEDONE,         ;; after finishing insert complete
    EVENT_CURSORHOLD,           ;; cursor in same position for a while
    EVENT_CURSORHOLDI,          ;; idem, in Insert mode
    EVENT_CURSORMOVED,          ;; cursor was moved
    EVENT_CURSORMOVEDI,         ;; cursor was moved in Insert mode
    EVENT_DIFFUPDATED,          ;; after diffs were updated
    EVENT_DIRCHANGED,           ;; after user changed directory
    EVENT_ENCODINGCHANGED,      ;; after changing the 'encoding' option
    EVENT_EXITPRE,              ;; before exiting
    EVENT_FILEAPPENDCMD,        ;; append to a file using command
    EVENT_FILEAPPENDPOST,       ;; after appending to a file
    EVENT_FILEAPPENDPRE,        ;; before appending to a file
    EVENT_FILECHANGEDRO,        ;; before first change to read-only file
    EVENT_FILECHANGEDSHELL,     ;; after shell command that changed file
    EVENT_FILECHANGEDSHELLPOST, ;; after (not) reloading changed file
    EVENT_FILEREADCMD,          ;; read from a file using command
    EVENT_FILEREADPOST,         ;; after reading a file
    EVENT_FILEREADPRE,          ;; before reading a file
    EVENT_FILETYPE,             ;; new file type detected (user defined)
    EVENT_FILEWRITECMD,         ;; write to a file using command
    EVENT_FILEWRITEPOST,        ;; after writing a file
    EVENT_FILEWRITEPRE,         ;; before writing a file
    EVENT_FILTERREADPOST,       ;; after reading from a filter
    EVENT_FILTERREADPRE,        ;; before reading from a filter
    EVENT_FILTERWRITEPOST,      ;; after writing to a filter
    EVENT_FILTERWRITEPRE,       ;; before writing to a filter
    EVENT_FOCUSGAINED,          ;; got the focus
    EVENT_FOCUSLOST,            ;; lost the focus to another app
    EVENT_FUNCUNDEFINED,        ;; if calling a function which doesn't exist
    EVENT_GUIENTER,             ;; after starting the GUI
    EVENT_GUIFAILED,            ;; after starting the GUI failed
    EVENT_INSERTCHANGE,         ;; when changing Insert/Replace mode
    EVENT_INSERTCHARPRE,        ;; before inserting a char
    EVENT_INSERTENTER,          ;; when entering Insert mode
    EVENT_INSERTLEAVE,          ;; when leaving Insert mode
    EVENT_MENUPOPUP,            ;; just before popup menu is displayed
    EVENT_OPTIONSET,            ;; option was set
    EVENT_QUICKFIXCMDPOST,      ;; after :make, :grep etc.
    EVENT_QUICKFIXCMDPRE,       ;; before :make, :grep etc.
    EVENT_QUITPRE,              ;; before :quit
    EVENT_REMOTEREPLY,          ;; upon string reception from a remote vim
    EVENT_SESSIONLOADPOST,      ;; after loading a session file
    EVENT_SHELLCMDPOST,         ;; after ":!cmd"
    EVENT_SHELLFILTERPOST,      ;; after ":1,2!cmd", ":w !cmd", ":r !cmd".
    EVENT_SOURCECMD,            ;; sourcing a Vim script using command
    EVENT_SOURCEPRE,            ;; before sourcing a Vim script
    EVENT_SOURCEPOST,           ;; after sourcing a Vim script
    EVENT_SPELLFILEMISSING,     ;; spell file missing
    EVENT_STDINREADPOST,        ;; after reading from stdin
    EVENT_STDINREADPRE,         ;; before reading from stdin
    EVENT_SWAPEXISTS,           ;; found existing swap file
    EVENT_SYNTAX,               ;; syntax selected
    EVENT_TABCLOSED,            ;; after closing a tab page
    EVENT_TABENTER,             ;; after entering a tab page
    EVENT_TABLEAVE,             ;; before leaving a tab page
    EVENT_TABNEW,               ;; when entering a new tab page
    EVENT_TERMCHANGED,          ;; after changing 'term'
    EVENT_TERMINALOPEN,         ;; after a terminal buffer was created
    EVENT_TERMRESPONSE,         ;; after setting "v:termresponse"
    EVENT_TEXTCHANGED,          ;; text was modified not in Insert mode
    EVENT_TEXTCHANGEDI,         ;; text was modified in Insert mode
    EVENT_TEXTCHANGEDP,         ;; TextChangedI with popup menu visible
    EVENT_TEXTYANKPOST,         ;; after some text was yanked
    EVENT_USER,                 ;; user defined autocommand
    EVENT_VIMENTER,             ;; after starting Vim
    EVENT_VIMLEAVE,             ;; before exiting Vim
    EVENT_VIMLEAVEPRE,          ;; before exiting Vim and writing .viminfo
    EVENT_VIMRESIZED,           ;; after Vim window was resized
    EVENT_WINENTER,             ;; after entering a window
    EVENT_WINLEAVE,             ;; before leaving a window
    EVENT_WINNEW,               ;; when entering a new window
    NUM_EVENTS                  ;; MUST be the last one
  ])

(def modes
  {0x01 'NORMAL
   (+ 0x100 0x01) 'NORMAL ;; NORMAL_BUSY
   0x04 'OP_PENDING
   0x10 'INSERT
   0x08 'COMMAND_LINE
   0x50 'REPLACE
   0x02 'VISUAL})

