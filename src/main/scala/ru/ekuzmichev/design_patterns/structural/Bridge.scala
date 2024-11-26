package ru.ekuzmichev.design_patterns.structural

import org.apache.commons.codec.binary.Hex

import java.security.MessageDigest

// The purpose of the bridge design pattern is to decouple an abstraction
// from its implementation so that the two can vary independently.
//
// The bridge design pattern is very similar to the adapter design pattern.
// The difference between them is that in the former, we apply it when
// we design our application, and the latter is used for legacy or third-party code.

object Bridge {
  object Basic {
    trait Hasher {
      def hash(data: String): String

      protected def getDigest(algorithm: String, data: String) = {
        val crypt = MessageDigest.getInstance(algorithm)
        crypt.reset()
        crypt.update(data.getBytes("UTF-8"))
        crypt
      }
    }

    class Sha1Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("SHA-1", data).digest()))
    }

    class Sha256Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("SHA-256", data).digest()))
    }

    class Md5Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("MD5", data).digest()))
    }

    abstract class PasswordConverter(hasher: Hasher) {
      def convert(password: String): String
    }

    class SimplePasswordConverter(hasher: Hasher) extends PasswordConverter(hasher) {
      override def convert(password: String): String =
        hasher.hash(password)
    }

    class SaltedPasswordConverter(salt: String, hasher: Hasher) extends PasswordConverter(hasher) {
      override def convert(password: String): String =
        hasher.hash(s"${salt}:${password}")
    }
  }

  object ScalaWay {
    trait Hasher {
      def hash(data: String): String

      protected def getDigest(algorithm: String, data: String) = {
        val crypt = MessageDigest.getInstance(algorithm)
        crypt.reset()
        crypt.update(data.getBytes("UTF-8"))
        crypt
      }
    }

    trait Sha1Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("SHA-1", data).digest()))
    }

    trait Sha256Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("SHA-256", data).digest()))
    }

    trait Md5Hasher extends Hasher {
      override def hash(data: String): String =
        new String(Hex.encodeHex(getDigest("MD5", data).digest()))
    }

    abstract class PasswordConverter {
      self: Hasher =>
      def convert(password: String): String
    }

    class SimplePasswordConverter extends PasswordConverter {
      self: Hasher =>
      override def convert(password: String): String = hash(password)
    }

    class SaltedPasswordConverter(salt: String) extends PasswordConverter {
      self: Hasher =>
      override def convert(password: String): String = hash(s"${salt}:${password}")
    }
  }
}

object BridgeExample {
  import Bridge.Basic._

  def main(args: Array[String]): Unit = {
    val p1: PasswordConverter = new SimplePasswordConverter(new Sha256Hasher)
    val p2: PasswordConverter = new SimplePasswordConverter(new Md5Hasher)
    val p3: PasswordConverter = new SaltedPasswordConverter("8jsdf32T^$%", new Sha1Hasher)
    val p4: PasswordConverter = new SaltedPasswordConverter("8jsdf32T^$%", new Sha256Hasher)

    System.out.println(s"'password' in SHA-256 is: ${p1.convert("password")}")
    System.out.println(s"'1234567890' in MD5 is: ${p2.convert("1234567890")}")
    System.out.println(s"'password' in salted SHA-1 is: ${p3.convert("password")}")
    System.out.println(s"'password' in salted SHA-256 is: ${p4.convert("password")}")
  }
}

object ScalaBridgeExample {
  import Bridge.ScalaWay._

  def main(args: Array[String]): Unit = {
    val p1: PasswordConverter = new SimplePasswordConverter with Sha256Hasher
    val p2: PasswordConverter = new SimplePasswordConverter with Md5Hasher
    val p3: PasswordConverter = new SaltedPasswordConverter("8jsdf32T^$%") with Sha1Hasher
    val p4: PasswordConverter = new SaltedPasswordConverter("8jsdf32T^$%") with Sha256Hasher

    System.out.println(s"'password' in SHA-256 is: ${p1.convert("password")}")
    System.out.println(s"'1234567890' in MD5 is: ${p2.convert("1234567890")}")
    System.out.println(s"'password' in salted SHA-1 is: ${p3.convert("password")}")
    System.out.println(s"'password' in salted SHA-256 is: ${p4.convert("password")}")
  }
}
