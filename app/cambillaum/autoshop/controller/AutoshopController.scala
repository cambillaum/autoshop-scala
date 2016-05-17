package cambillaum.autoshop.controller

import cambillaum.autoshop.dto.VehiclesPageDTO
import cambillaum.autoshop.service.VehiclesPageService
import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

class AutoshopController @Inject() (vehiclesPageService: VehiclesPageService) extends Controller {

  def loadVehiclesPage = Action { request =>
    val maybeNumberOfVehiclesPerPageString = request.getQueryString("numberOfVehiclesPerPage")
    val maybeNumberOfVehiclesPerPage = maybeNumberOfVehiclesPerPageString.map(_.toInt)
    val maybeOffsetString = request.getQueryString("offset")
    val maybeOffset = maybeOffsetString.map(_.toInt)
    maybeNumberOfVehiclesPerPage.map(numberOfVehiclesPerPage => {
      maybeOffset.map(offset => {
        val vehiclesPageDTO = vehiclesPageService.loadVehiclesPage(numberOfVehiclesPerPage, offset)
        val jsonBody = Json.toJson(vehiclesPageDTO)
        Ok(jsonBody)
      }).getOrElse(BadRequest("offset query param is mandatory"))
    }).getOrElse(BadRequest("numberOfVehiclesPerPage param is mandatory"))
  }

}
