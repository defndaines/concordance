# Concordance

A Clojure library designed to tell how often words appear in a text.

For the purposes of this library, a "word" is a sequence of letters, numbers, or
an apostrophe. All punctuation and white space is ignored (except for the
aforementioned apostrophe). The apostrophe is treated as part of a word to
avoid "don't" being turned into the nonsensical "don" and "t".


## Usage

A [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is
required to run and develop this application. To build yourself, you will need
to install [lein](https://leiningen.org/).

### Build an Uberjar

An "uberjar" is a single JAR file containing all required libraries which can be
invoked relatively easily from the command line.

```shell
$ lein clean
$ lein uberjar
```

This will generate the file under `target/concordance.jar`.


### Running Concordance from the Command Line

Once you have the `concordance.jar`, you can run it from the command line using
the following.

```shell
$ java -jar concordance.jar path/to/file.txt > results.txt
```

Output includes the word, a space, then the number of times the word appears in
the text.

Output will be directed to standard out, so be sure to pipe to a file.

#### Options

If you pass the `--help` flag, you can see the command-line options.

```shell
$ java -jar concordance.jar --help

Utility for counting the frequency of words in a text.

Usage: concordance [-s ORDER] text.txt

Options:
  -s, --sort ORDER  alpha  Sorting order. Must be one of "alpha" or "freq".
  -h, --help
```

#### Alphabetical Sorting

If no sorting option is passed, output will be alphabetical by default.

```shell
$ java -jar concordance.jar common-sense.txt

'tis 9
a 451
ability 2
able 11
ablest 1
abound 1
about 5
above 5
abroad 2
abrupt 1
...
```

#### Frequency Sorting

If `-s freq` is passed, the output will be sorted by the most frequent words
first. When multiple words have the same frequency, they will be sorted
alphabetically.

```shell
$ java -jar concordance.jar -s freq les-misérable.txt

the 40569
of 19655
and 14788
a 14396
to 13777
in 11058
he 9588
was 8609
that 7778
it 6506
his 6444
...
```


### API

When called from Clojure code, the library exposes a `word-count` function, as
well as the sorting functions, `alphabetical-order` and `frequency-order`.

#### `word-count`

Word count accepts a single string or a sequnce of strings and returns a map of
words to frequency values.

```clojure
(require '[concordance.core :as concordance])
(def meditation "No man is an island entire of itself; every man
                is a piece of the continent, a part of the main;
                if a clod be washed away by the sea, Europe
                is the less, as well as if a promontory were, as
                well as any manner of thy friends or of thine
                own were; any man's death diminishes me,
                because I am involved in mankind.
                And therefore never send to know for whom
                the bell tolls; it tolls for thee.")
(def counts (concordance/word-count meditation))

{"itself" 1 "thine" 1 "of" 5 "involved" 1 "continent" 1 "part" 1
 "promontory" 1 "every" 1 "it" 1 "send" 1 "by" 1 "is" 3 "europe" 1 "away" 1
 "sea" 1 "friends" 1 "for" 2 "thy" 1 "whom" 1 "therefore" 1 "because" 1
 "any" 2 "were" 2 "main" 1 "if" 2 "man" 2 "diminishes" 1 "an" 1 "or" 1
 "am" 1 "a" 4 "tolls" 2 "never" 1 "own" 1 "manner" 1 "bell" 1 "death" 1
 "thee" 1 "entire" 1 "be" 1 "and" 1 "piece" 1 "i" 1 "less" 1 "island" 1
 "no" 1 "well" 2 "clod" 1 "washed" 1 "to" 1 "mankind" 1 "know" 1 "as" 4
 "me" 1 "the" 5 "in" 1 "man's" 1}
```

#### Comparators

The exposed
[Comparator](http://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)
function is designed to work with the core `sort-by` function.

```clojure
(sort-by concordance/frequency-order counts)

(["of" 5] ["the" 5] ["a" 4] ["as" 4] ["is" 3] ...)
```


## Performance

*This Branch* represents an approach using a mutable `TreeMap` to aggregate
frequencies across each line of a file (instead of reading in the entire file at
once). The time performance improvement is minimal,
dropping from 1.6 seconds to 1.5 seconds to process "Les Misérables".

From a maintenance perspective, this version is more complicated. Additionally,
it doesn't naturally match the API of the first version, so `word-count` has to
handle all the logic of both performing the word count and sorting to
preference.

This version is more efficient in memory, since it doesn't need to hold the
whole file in memory to create the map. It can also store the word counts in a
sorted collection, so does not incur any additional cost of sorting when the
user has requested alphabetical ordering.


## License

Copyright © 2017 Michael S. Daines

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
