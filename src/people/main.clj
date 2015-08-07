(ns people.main
  (require [clojure.tools.cli :refer [parse-opts]]
           [clojure.string :as string]
           [clojure.java.io :as io]
           [people.tokenize :refer [tokenize]]
           [people.parse :refer [parse]]
           [people.report :refer [print-report]])
  (:gen-class))

(defn usage [options-summary]
  (->> ["Print people report."
        ""
        "Usage: people -r REPORT-NUMBER file"
        ""
        "Options:"
        options-summary
        ""
        "Report number:"
        ""
        "1 - sorted by gender (females before males) then by last name ascending"
        "2 - sorted by birth date, ascending"
        "3 - sorted by last name, descending"
        ""
        "File: person records, one per line, separated by commas, pipes (|), or spaces."
        "  Each record has the following five fields:"
        "  LastName,FirstName,Gender,FavoriteColor,DateOfBirth"
        "    Gender: either \"M\" or \"F\""
        "    DateOfBirth: a date such as \"2000-12-31\""]
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

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond errors             (exit 1 (error-msg errors))
          (empty? arguments) (exit 1 (usage summary))
          (:help options)    (exit 0 (usage summary))
          :else              (println (print-report (-> (first arguments) io/reader tokenize parse) (:report options))))))

