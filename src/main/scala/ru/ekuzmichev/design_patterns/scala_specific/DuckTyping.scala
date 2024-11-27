package ru.ekuzmichev.design_patterns.scala_specific

import java.util.StringTokenizer

// Duck typing is a term that comes from dynamic languages, and
// it allows us to treat different types of objects in a similar
// manner based on a common method they have.
//
// Another name for duck typing is structural typing.
//
object DuckTyping {
  class SentenceParserTokenize {
    def parse(sentence: String): Array[String] = {
      val tokenizer = new StringTokenizer(sentence)
      Iterator
        .continually({
          val hasMore = tokenizer.hasMoreTokens
          if (hasMore) {
            (hasMore, tokenizer.nextToken())
          } else {
            (hasMore, null)
          }
        })
        .takeWhile(_._1)
        .map(_._2)
        .toArray
    }
  }

  class SentenceParserSplit {
    def parse(sentence: String): Array[String] = sentence.split("\\s")
  }
}

object DuckTypingExample {
  import DuckTyping._

  def printSentenceParts(
      sentence: String,
      parser: {
        def parse(sentence: String): Array[String]
      }
  ): Unit = parser.parse(sentence).foreach(println)

  def main(args: Array[String]): Unit = {
    val tokenizerParser = new SentenceParserTokenize
    val splitParser     = new SentenceParserSplit

    val sentence = "This is the sentence we will be splitting."

    println("Using the tokenize parser: ")
    printSentenceParts(sentence, tokenizerParser)

    println("Using the split parser: ")
    printSentenceParts(sentence, splitParser)
  }
}
