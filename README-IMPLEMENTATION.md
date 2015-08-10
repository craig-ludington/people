# people

This application was built with Clojure using the Ring and Compojure libraries.

There's a command-line application that accepts three different input formats and prints three different types of reports.

There's a web API that accepts input records and returns JSON for the three reports.

## Build

To build the command-line version, run:

	lein bin

The output executable file is ~/bin/people.

To build a WAR file (suitable for the Tomcat application server, run:

	lein ring uberwar

The output war file is target/people-0.1.0-SNAPSHOT-standalone.war

To just start the web server for development, run:

	lein ring server

The webserver will listen on http://localhost:3000/

## Test

To run the unit tests, run:

	lein test

To run the integration tests, make sure you build the people executable first with "lein bin", then run:

	test/integration/test-command-line

A bash script runs the command-line integration tests.

## Command-line program options

The command-line version of the program accepts multiple input files (each file may use a different delimiter),
or reads from standard input if no files are specified.

The ouput is one of the three reports.  You can specifiy the report number (1, 2, or 3) with the -r option.

To see the options, run the program with the -h or --help option:

	people --help

You'll see output like this:

```
Print people report.

Usage: people [-r REPORT-NUMBER] [file ...]

Options:
  -r, --report REPORT-NUMBER  1  The selected report - 1, 2, or 3
  -h, --help

Optional report number (default is 1):

1 - sorted by gender (females before males) then by last name ascending
2 - sorted by birth date, ascending
3 - sorted by last name, descending

Optional file (or files): person records, one per line, separated by commas, pipes (|), or spaces.
  Each record has the following five fields:
  LastName,FirstName,Gender,FavoriteColor,DateOfBirth
    Gender: either "M" or "F"
    DateOfBirth: a date, e.g. "2000-12-31", or "12/31/2000".

If no file argument is supplied, standard input is used instead.
```

## Program structure

The program is organized as:

### main.clj
The entry point for the command-line version.  It handles the options and file arguments,
parses and stores the inputs and finally prints a report.

### handler.clj
The entry point for the web api version.  It sets up the routes and defers HTTP requests to the API handlers.

### api.clj
Functions to take incoming requests and return JSON responses.

### date.clj
A little bit of date parsing so we can handle most standard date formats, as well as the MM/DD/YYYY format that we output in the reports.

### tokenize.clj
Converts the input files (or records from the web API) to tokens that we can parse into the program's semantics.

### parse.clj
Converts tokens into maps of data with dates parsed for convenient sorting.

### store.clj
A trivial in-memory data store that stores the records created by parse.clj and returns them.
It stores records as a set so no duplicate records are stored.  (Parsing and storing the same input file is idempotent.)

### report.clj
Queries the data store returning results ordered for one of the three types of report.
It returns the results as comma-separated values, or as JSON.



