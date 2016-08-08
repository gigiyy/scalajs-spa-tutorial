package spatutorial.client.services

import autowire._
import diode.util._
import boopickle.Default._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import diode.data.{Empty, Pot, PotAction}
import diode.util.RunAfterJS
import diode.{ActionHandler, ModelRW}
import spatutorial.shared.Api

/**
  * Handles actions related to the Motd
  *
  * @param modelRW Reader/Writer to access the model
  */
class MotdHandler[M](modelRW: ModelRW[M, Pot[String]]) extends ActionHandler(modelRW) {
  implicit val runner = new RunAfterJS

  override def handle = {
    case action: UpdateMotd =>
      val updateF = action.effect(AjaxClient[Api].welcomeMsg("User X").call())(identity)
      action.handleWith(this, updateF)(PotAction.handler())
  }
}

case class UpdateMotd(potResult: Pot[String] = Empty) extends PotAction[String, UpdateMotd] {
  override def next(value: Pot[String]) = UpdateMotd(value)
}


