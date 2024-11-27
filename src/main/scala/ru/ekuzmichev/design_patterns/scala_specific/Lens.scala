package ru.ekuzmichev.design_patterns.scala_specific

import scalaz.{Lens => ScalazLens}

// The lens design pattern allows to overcome the immutability limitation and
// at the same time preserve the code's readability.
//
// The lens design pattern allows us to cleanly set properties of our case class
// without breaking the immutability rule.
// We simply need to define the right lenses and then use them.
//
object Lens {

  // Here we use scalaz lens library
  // For less boilerplate one could use Monocle library

  case class Country(name: String, code: String)
  case class City(name: String, country: Country)
  case class Address(number: Int, street: String, city: City)
  case class Company(name: String, address: Address)
  case class User(name: String, company: Company, address: Address)

  object User {
    val userCompany = ScalazLens.lensu[User, Company](
      (u, company) => u.copy(company = company),
      _.company
    )
    val userAddress = ScalazLens.lensu[User, Address](
      (u, address) => u.copy(address = address),
      _.address
    )
    val companyAddress = ScalazLens.lensu[Company, Address](
      (c, address) => c.copy(address = address),
      _.address
    )
    val addressCity = ScalazLens.lensu[Address, City](
      (a, city) => a.copy(city = city),
      _.city
    )

    val cityCountry = ScalazLens.lensu[City, Country](
      (c, country) => c.copy(country = country),
      _.country
    )
    val countryCode = ScalazLens.lensu[Country, String](
      (c, code) => c.copy(code = code),
      _.code
    )
    // >=> - alias for andThen
    val userCompanyCountryCode = userCompany >=> companyAddress >=> addressCity >=> cityCountry >=> countryCode
  }
}

object LensExample {
  import Lens._

  def main(args: Array[String]): Unit = {

    val uk               = Country("United Kingdom", "uk")
    val london           = City("London", uk)
    val buckinghamPalace = Address(1, "Buckingham Palace Road", london)
    val castleBuilders   = Company("Castle Builders", buckinghamPalace)
    val switzerland      = Country("Switzerland", "CH")
    val geneva           = City("geneva", switzerland)
    val genevaAddress    = Address(1, "Geneva Lake", geneva)
    val ivan             = User("Ivan", castleBuilders, genevaAddress)

    println(User.userCompanyCountryCode.mod(_.toUpperCase, ivan))
  }
}
