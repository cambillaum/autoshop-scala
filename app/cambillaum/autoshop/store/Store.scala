package cambillaum.autoshop.store

import java.time.Year

import cambillaum.autoshop.domain._

trait VehiclesStore {

  def loadAllVehicles: Set[Vehicle]

}

class MockVehiclesStore extends VehiclesStore {

  private val vehicles: Set[Vehicle] = Set(
    Car("car1", "Car For Sale 1", 1000, Year.of(2000), Sedan, 3),
    Car("car2", "Car For Sale 2", 20000, Year.of(2015), Suv, 5),
    Car("car3", "Car For Sale 3", 5000, Year.of(2007), Truck, 5),
    Motorcycle("motorcycle1", "Motorcycle For Sale 1", 5000, Year.of(2013), false),
    Motorcycle("motorcycle2", "Motorcycle For Sale 2", 3000, Year.of(2010), false),
    Motorcycle("motorcycle3", "Motorcycle For Sale 3", 12000, Year.of(2015), true)
  )

  override def loadAllVehicles: Set[Vehicle] = {
    vehicles
  }

}
