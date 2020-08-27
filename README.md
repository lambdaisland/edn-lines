# lambdaisland/edn-lines

<!-- badges -->
[![CircleCI](https://circleci.com/gh/lambdaisland/edn-lines.svg?style=svg)](https://circleci.com/gh/lambdaisland/edn-lines) [![cljdoc badge](https://cljdoc.org/badge/lambdaisland/edn-lines)](https://cljdoc.org/d/lambdaisland/edn-lines) [![Clojars Project](https://img.shields.io/clojars/v/lambdaisland/edn-lines.svg)](https://clojars.org/lambdaisland/edn-lines)
<!-- /badges -->

Extensible Data Notation or [EDN](https://github.com/edn-format/edn) is a rich
data format that is popular in the Clojure world.

This library contains helper functions for reading and writing files of newline-separated EDN values.

<!-- opencollective -->

&nbsp;

<img align="left" src="https://github.com/lambdaisland/open-source/raw/master/artwork/lighthouse_readme.png">

&nbsp;

## Support Lambda Island Open Source

edn-lines is part of a growing collection of quality Clojure libraries and
tools released on the Lambda Island label. If you are using this project
commercially then you are expected to pay it forward by
[becoming a backer on Open Collective](http://opencollective.com/lambda-island#section-contribute),
so that we may continue to enjoy a thriving Clojure ecosystem.

&nbsp;

&nbsp;

<!-- /opencollective -->

## Installation

deps.edn

```
lambdaisland/edn-lines {:mvn/version "0.0-5"}
```

project.clj

```
[lambdaisland/edn-lines "0.0-5"]
```

## Rationale

An edn-lines file (extension `.ednl`) is a file which contains one or more EDN
collection forms, be it vectors, lists, or maps, with each form represented as a
single line of text, and lines separated by newline (`"\n"`) characters.

This way it is trivial to append to a file (for instance log messages or
events), since there is no wrapping collection, and because one line = one form
it plays nicely with traditional line-oriented tools like `grep`, `head`,
`tail`, etc. If you grep an arbitrary EDN file you are unlikely to get valid EDN
back, whereas you can grep an `ednl` file just fine.

## Usage

``` clojure
(require '[lambdaisland.edn-lines :as ednl])

(ednl/spit "test.ednl" [{:foo 1} {:foo 2} {:foo 3}])

(ednl/slurp "test.ednl") ;; lazy seq of values

(ednl/with-append [append "test.ednl"]
  (append {:foo 4})
  (append {:foo 5}))
```

## Tagged literals

To print/read custom types, use the extension mechanisms provided by Clojure
itself:

- for printing extend the `clojure.core/print-method` multimethod
- for reading add reader functions to `clojure.core/*data-readers*` (or provide a
  `data_readers.clj(c)` on the classpath)

Printing happens with `pr`, reading happens with `clojure.tools.reader.edn`, but
with the reader functions from `clojure.core/*data-readers*` passed in.

## Use with Babashka

When dealing with `ednl` files in the shell [bb
-IO](https://github.com/borkdude/babashka) is your friend


```
bb -IO '(filter #(< 1 (:foo %) 4) *input*)' < test.ednl
```

<!-- contributing -->
## Contributing

Everyone has a right to submit patches to edn-lines, and thus become a contributor.

Contributors MUST

- adhere to the [LambdaIsland Clojure Style Guide](https://nextjournal.com/lambdaisland/clojure-style-guide)
- write patches that solve a problem. Start by stating the problem, then supply a minimal solution. `*`
- agree to license their contributions as MPL 2.0.
- not break the contract with downstream consumers. `**`
- not break the tests.

Contributors SHOULD

- update the CHANGELOG and README.
- add tests for new functionality.

If you submit a pull request that adheres to these rules, then it will almost
certainly be merged immediately. However some things may require more
consideration. If you add new dependencies, or significantly increase the API
surface, then we need to decide if these changes are in line with the project's
goals. In this case you can start by [writing a pitch](https://nextjournal.com/lambdaisland/pitch-template),
and collecting feedback on it.

`*` This goes for features too, a feature needs to solve a problem. State the problem it solves, then supply a minimal solution.

`**` As long as this project has not seen a public release (i.e. is not on Clojars)
we may still consider making breaking changes, if there is consensus that the
changes are justified.
<!-- /contributing -->

<!-- license -->
## License

Copyright &copy; 2020 Arne Brasseur and Contributors

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
<!-- /license -->