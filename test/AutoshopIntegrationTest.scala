import cambillaum.autoshop.dto.{CarDTO, MotorcycleDTO, VehicleCategoryWithNumberDTO, VehiclesPageDTO}
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.test._
import play.api.test.Helpers.{GET => GET_REQUEST, _}

class AutoshopIntegrationTest extends PlaySpec with OneServerPerSuite with FutureAwaits with DefaultAwaitTimeout {

  "The autoshop VehiclesPage Rest Service" must {
    "Return the expected JSON response" in {
      val wsClient = app.injector.instanceOf[WSClient]
      val vehiclesPageServiceUrl = s"http://localhost:$port/vehicles/page"
      val response = await(wsClient.url(vehiclesPageServiceUrl).withQueryString("numberOfVehiclesPerPage" -> 3.toString, "offset" -> 0.toString).get())
      response.status.mustBe(OK)
      val vehiclesPageDTO = Json.fromJson[VehiclesPageDTO](response.json).get
      val expectedVehiclesPageDTO = VehiclesPageDTO(
        totalNumberOfVehicles = 6,
        numberOfVehiclesByCategory = List(
          VehicleCategoryWithNumberDTO("SUV", 1),
          VehicleCategoryWithNumberDTO("Sedan", 1),
          VehicleCategoryWithNumberDTO("Truck", 1),
          VehicleCategoryWithNumberDTO("Motorcycle", 3)
        ),
        vehiclesInPage = List(
          CarDTO("car1", "Car For Sale 1", 1000, 2000, "SEDAN", 3),
          MotorcycleDTO("motorcycle2", "Motorcycle For Sale 2", 3000, 2010, false),
          CarDTO("car3", "Car For Sale 3", 5000, 2007, "TRUCK", 5)
        )
      )
      vehiclesPageDTO.mustBe(expectedVehiclesPageDTO)
    }
  }

}
