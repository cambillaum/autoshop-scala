package cambillaum.autoshop.domain

import java.time.Year

sealed trait Vehicle {
  def id: String
  def name: String
  def dollarPrice: Double
  def modelYear: Year
}

case class Motorcycle(id: String, name: String, dollarPrice: Double, modelYear: Year, hasSidecar: Boolean) extends Vehicle

sealed trait CarType {
  def name: String
}
case object Sedan extends CarType {
  val name: String = "SEDAN"
}
case object Suv extends CarType {
  val name: String = "SUV"
}
case object Truck extends CarType {
  val name: String = "TRUCK"
}

case class Car(id: String, name: String, dollarPrice: Double, modelYear: Year, carType: CarType, doorsNumber: Int) extends Vehicle