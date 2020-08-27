(ns lambdaisland.edn-lines
  (:refer-clojure :exclude [slurp spit])
  (:require [clojure.java.io :as io]
            [clojure.tools.reader.edn :as edn]
            [clojure.tools.reader.reader-types :as reader-types]))

(defn append
  "Append a single form to a writer, followed by a newline."
  [writer value]
  (binding [*out* writer]
    (prn value)))

(defmacro with-append
  "Open a file in append mode, and add forms to it, using the append function
  which gets bound in the binding form.

  The file is closed at the end of the scope.

  (with-append [append \"outfile.ednl\"]
    (append [1 2 3])
    (append [:x :y :z]))
  "
  [[append-binding file] & body]
  `(with-open [f# (io/writer ~file :append true)]
     (let [~append-binding (partial append f#)]
       ~@body)))

(defn- read-next [reader]
  (try
    (let [x (edn/read reader false ::eof {:readers *data-readers*})]
      (if (= ::eof x)
        (do
          (.close reader)
          nil)
        x))
    (catch Throwable t
      (.close reader)
      (throw t))))

(defn reader-seq
  "Given a PushBackReader, return a lazy seq of forms."
  [reader]
  (take-while identity (repeatedly #(read-next reader))))

(defn slurp
  "Read in an ednl file. Returns a lazy sequence of EDN values."
  [f]
  (->  f
       io/reader
       reader-types/push-back-reader
       reader-seq))

(defn spit
  "Write out a sequence of EDN values to a file.

  Options:
  - `:append?` append to the file, instead of overwriting it. Defaults to `false`."
  [f contents & [{:keys [append?] :or {append? false} :as options}]]
  (with-open [out (io/writer (io/file f) :append append?)]
    (run! (partial append out) contents)))
