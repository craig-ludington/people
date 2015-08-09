(ns people.main
  (require [clojure.tools.cli :refer [parse-opts]]
           [clojure.string :as string]
           [clojure.java.io :as io]
           [clojure.tools.logging :as log]
           [people.tokenize :refer [tokenize]]
           [people.parse :refer [parse]]
           [people.report :refer [print-report]]
           [people.store :as store :refer [store fetch]])
  (:gen-class))

(defn usage [options-summary]
  (->> ["Print people report."
        ""
        "Usage: people [-r REPORT-NUMBER] [file ...]"
        ""
        "Options:"
        options-summary
        ""
        "Optional report number (default is 1):"
        ""
        "1 - sorted by gender (females before males) then by last name ascending"
        "2 - sorted by birth date, ascending"
        "3 - sorted by last name, descending"
        ""
        "Optional file (or files): person records, one per line, separated by commas, pipes (|), or spaces."
        "  Each record has the following five fields:"
        "  LastName,FirstName,Gender,FavoriteColor,DateOfBirth"
        "    Gender: either \"M\" or \"F\""
        "    DateOfBirth: a date, e.g. \"2000-12-31\", or \"12/31/2000\"."
        ""
        "If no file argument is supplied, standard input is used instead."]
       (string/join \newline)))

(def cli-options
  [["-r" "--report REPORT-NUMBER" "The selected report - 1, 2, or 3"
    :default 1
    :parse-fn #(Integer/parseInt %)
    :validate [#(#{1 2 3} %)]]
   ["-h" "--help"]])

(defn error-msg [errors]
  (str "Error processing command-line:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn print-report-from-stdin
  [report-number]
  (log/debug (str "print-report-from-stdin: report-number" report-number))
  (println (print-report (-> *in* tokenize parse) report-number)))

(defn parse-and-store-from-file
  [file]
  (log/debug (str "parse-and-store-from-file: " file))
  (store (-> file io/reader tokenize parse)))

(defn print-report-from-files
  [files report-number]
  (log/debug (str "print-report-from-files: files: " files " report-number: " report-number))
  (doall (map parse-and-store-from-file files))
  (log/debug (str "print-report-from-files: @store/data: " @store/data))
  (println (print-report (fetch) report-number)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond errors          (exit 1 (error-msg errors))
          (:help options) (exit 0 (usage summary))
          :else           (if (empty? arguments)
                            (print-report-from-stdin (:report options))
                            (print-report-from-files arguments (:report options))))))

              
