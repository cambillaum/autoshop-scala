package cambillaum.autoshop.module

import cambillaum.autoshop.service.{VehiclesPageService, VehiclesPageServiceImpl}
import cambillaum.autoshop.store.{MockVehiclesStore, VehiclesStore}
import com.google.inject.AbstractModule

class AutoshopModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[VehiclesStore]).to(classOf[MockVehiclesStore]).asEagerSingleton()
    bind(classOf[VehiclesPageService]).to(classOf[VehiclesPageServiceImpl]).asEagerSingleton()
  }
}
