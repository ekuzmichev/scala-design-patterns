package ru.ekuzmichev.design_patterns.scala_specific

import org.apache.commons.codec.binary.Hex

import java.security.MessageDigest
import scala.collection.mutable

// Memoization is a mechanism of recording a function result
// based on its arguments in order to reduce computation in consecutive calls.
//
object Memoization {
  class Hasher extends Memoizer {
    def md5(input: String): String = {
      println(s"Calling md5 for $input.")
      new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest(input.getBytes)))
    }

    val memoMd5 = memo(md5)
  }

  trait Memoizer {
    def memo[X, Y](f: X => Y): (X => Y) = {
      val cache = mutable.Map[X, Y]()
      (x: X) => cache.getOrElseUpdate(x, f(x))
    }
  }

  // Scalaz alternative:
  // val memoMd5Scalaz: String => String = Memo.immutableHashMapMemo {
  //   md5
  // }
}

object MemoizationExample {
  import Memoization._

  def main(args: Array[String]): Unit = {
    val hasher = new Hasher
    println(s"MD5 for 'hello' is '${hasher.memoMd5("hello")}'.")
    println(s"MD5 for 'bye' is '${hasher.memoMd5("bye")}'.")
    println(s"MD5 for 'hello' is '${hasher.memoMd5("hello")}'.")
    println(s"MD5 for 'bye1' is '${hasher.memoMd5("bye1")}'.")
    println(s"MD5 for 'bye' is '${hasher.memoMd5("bye")}'.")
  }
}
