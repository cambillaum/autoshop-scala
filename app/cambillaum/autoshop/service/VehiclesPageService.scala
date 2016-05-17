package cambillaum.autoshop.service

import cambillaum.autoshop.domain.{Car, Motorcycle, Sedan, Suv, Truck, Vehicle}
import cambillaum.autoshop.dto._
import cambillaum.autoshop.store.VehiclesStore
import com.google.inject.Inject

trait VehiclesPageService {
  def loadVehiclesPage(numberOfVehiclesPerPage: Int, offset: Int): VehiclesPageDTO
}

class VehiclesPageServiceImpl @Inject() (vehiclesStore: VehiclesStore) extends VehiclesPageService {
  override def loadVehiclesPage(numberOfVehiclesPerPage: Int, offset: Int): VehiclesPageDTO = {
    val allVehicles = vehiclesStore.loadAllVehicles

    val totalNumberOfVehicles = allVehicles.size

    val numberOfVehiclesByCategory = extractNumberOfVehiclesByCategory(allVehicles)
    val allVehiclesByAscendingPriceAndId = sortByAscendingPriceAndId(allVehicles)
    val vehiclesOnPage = filterVehiclesOnPage(allVehiclesByAscendingPriceAndId, numberOfVehiclesPerPage, offset)
    val vehiclesInPage = transformToDTOs(vehiclesOnPage)

    VehiclesPageDTO(totalNumberOfVehicles, numberOfVehiclesByCategory, vehiclesInPage)
  }

  private def extractNumberOfVehiclesByCategory(vehicles: Set[Vehicle]): List[VehicleCategoryWithNumberDTO] = {
    val categorizedVehicles = vehicles.groupBy(
      _ match {
        case car: Car => car.carType match {
          case Sedan => "Sedan"
          case Suv => "SUV"
          case Truck => "Truck"
        }
        case motorcycle: Motorcycle => "Motorcycle"
      }
    )

    val categoriesWithNumbers = categorizedVehicles.map {
      case (categoryName, vehiclesInCategory) => VehicleCategoryWithNumberDTO(categoryName, vehiclesInCategory.size)
    }.toList

    categoriesWithNumbers.sortBy(categoryWithNumber => (categoryWithNumber.numberOfVehicles, categoryWithNumber.category))
  }

  private def sortByAscendingPriceAndId(vehicles: Set[Vehicle]): List[Vehicle] = {
    vehicles.toList.sortBy(vehicle => (vehicle.dollarPrice, vehicle.id))
  }

  private def filterVehiclesOnPage(vehicles: List[Vehicle], numberOfVehiclesPerPage: Int, offset: Int): List[Vehicle] = {
    vehicles.drop(offset).take(numberOfVehiclesPerPage)
  }

  private def transformToDTOs(vehicles: List[Vehicle]): List[VehicleDTO] = {
    vehicles.map {
      _ match {
        case car: Car => CarDTO(car.id, car.name, car.dollarPrice, car.modelYear.getValue, car.carType.name, car.doorsNumber)
        case motorcycle: Motorcycle => MotorcycleDTO(motorcycle.id, motorcycle.name, motorcycle.dollarPrice, motorcycle.modelYear.getValue, motorcycle.hasSidecar)
      }
    }
  }

}
