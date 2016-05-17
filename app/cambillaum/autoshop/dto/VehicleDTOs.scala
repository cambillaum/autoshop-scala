package cambillaum.autoshop.dto

import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait VehicleDTO {
  def vehicleType: String
  def id: String
  def name: String
  def dollarPrice: Double
  def modelYear: Int
}
object VehicleDTO {
  implicit val vehicleDTOFormat: Format[VehicleDTO] = new Format[VehicleDTO] {
    override def reads(json: JsValue): JsResult[VehicleDTO] = {
      val vehicleType = (json \ "vehicleType").validate[String] match {
        case success: JsSuccess[String] => success.value
        case error: JsError => throw new IllegalArgumentException(s"Json parsing error ${JsError.toJson(error)}")
      }

      vehicleType match {
        case "car" => Json.fromJson[CarDTO](json)
        case "motorcycle" => Json.fromJson[MotorcycleDTO](json)
        case _ => throw new IllegalArgumentException(s"Unexpected vehicleType $vehicleType")
      }
    }

    override def writes(vehicleDTO: VehicleDTO): JsValue = vehicleDTO match {
      // Writes[-T] is contravariant and scala resolves the implicit as vehicleDTOFormat, got to pass the implicit Writes[CarDTO] and Writes[MotorcycleDTO] explicitly to avoid stackoverflow
      case carDTO: CarDTO => Json.toJson(carDTO)(CarDTO.carDTOFormat)
      case motorcycleDTO: MotorcycleDTO => Json.toJson(motorcycleDTO)(MotorcycleDTO.motorcycleDTOFormat)
    }
  }
}

case class CarDTO(id: String, name: String, dollarPrice: Double, modelYear: Int, carType: String, doorsNumber: Int) extends VehicleDTO {
  override def vehicleType: String = "car"
}
object CarDTO {
  private def toCarDTO(id: String, name: String, dollarPrice: Double, modelYear: Int, carType: String, doorsNumber: Int, vehicleType: String): CarDTO =
    CarDTO(id, name, dollarPrice, modelYear, carType, doorsNumber)

  private def fromCarDTO(carDTO: CarDTO): (String, String, Double, Int, String, Int, String) =
    (carDTO.id, carDTO.name, carDTO.dollarPrice, carDTO.modelYear, carDTO.carType, carDTO.doorsNumber, carDTO.vehicleType)

  implicit val carDTOFormat: Format[CarDTO] = (
    (JsPath \ "id").format[String] and
    (JsPath \ "name").format[String] and
    (JsPath \ "dollarPrice").format[Double] and
    (JsPath \ "modelYear").format[Int] and
    (JsPath \ "carType").format[String] and
    (JsPath \ "doorsNumber").format[Int] and
    (JsPath \ "vehicleType").format[String]
  )(toCarDTO _, fromCarDTO)
}

case class MotorcycleDTO(id: String, name: String, dollarPrice: Double, modelYear: Int, hasSidecar: Boolean) extends VehicleDTO {
  override def vehicleType: String = "motorcycle"
}
object MotorcycleDTO {
  private def toMotorcycleDTO(id: String, name: String, dollarPrice: Double, modelYear: Int, hasSidecar: Boolean, vehicleType: String): MotorcycleDTO =
    MotorcycleDTO(id, name, dollarPrice, modelYear, hasSidecar)

  private def fromMotorcycleDTO(motorcycleDTO: MotorcycleDTO): (String, String, Double, Int, Boolean, String) =
    (motorcycleDTO.id, motorcycleDTO.name, motorcycleDTO.dollarPrice, motorcycleDTO.modelYear, motorcycleDTO.hasSidecar, motorcycleDTO.vehicleType)

  implicit val motorcycleDTOFormat: Format[MotorcycleDTO] = (
    (JsPath \ "id").format[String] and
    (JsPath \ "name").format[String] and
    (JsPath \ "dollarPrice").format[Double] and
    (JsPath \ "modelYear").format[Int] and
    (JsPath \ "hasSidecar").format[Boolean] and
    (JsPath \ "vehicleType").format[String]
  )(toMotorcycleDTO _, fromMotorcycleDTO _)
}

case class VehicleCategoryWithNumberDTO(category: String, numberOfVehicles: Int)
object VehicleCategoryWithNumberDTO {
  implicit val vehicleCategoryWithNumberDTOFormat: Format[VehicleCategoryWithNumberDTO] = (
    (JsPath \ "category").format[String] and
    (JsPath \ "numberOfVehicles").format[Int]
  )(VehicleCategoryWithNumberDTO.apply _, unlift(VehicleCategoryWithNumberDTO.unapply _))
}

case class VehiclesPageDTO(totalNumberOfVehicles: Int, numberOfVehiclesByCategory: List[VehicleCategoryWithNumberDTO], vehiclesInPage: List[VehicleDTO])
object VehiclesPageDTO {
  implicit val vehiclesPageDTOFormat: Format[VehiclesPageDTO] = (
    (JsPath \ "totalNumberOfVehicles").format[Int] and
    (JsPath \ "numberOfVehiclesByCategory").format[List[VehicleCategoryWithNumberDTO]] and
    (JsPath \ "vehiclesInPage").format[List[VehicleDTO]]
  )(VehiclesPageDTO.apply _, unlift(VehiclesPageDTO.unapply _))
}

