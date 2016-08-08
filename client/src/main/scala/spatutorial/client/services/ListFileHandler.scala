package spatutorial.client.services

import autowire._
import diode.util._
import boopickle.Default._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import diode.data.{Pot, Ready}
import diode.{Action, ActionHandler, Effect, ModelRW}
import spatutorial.shared.{Api, FileItem}

/**
  * Created by GiGiYY on 8/8/2016.
  */
class ListFileHandler[M](modelRW: ModelRW[M, Pot[FileItems]]) extends ActionHandler(modelRW) {
  override def handle = {
    case ListAllFiles(path) =>
      effectOnly(Effect(AjaxClient[Api].listFile(path).call().map(UpdateListFiles)))
    case UpdateListFiles(items) =>
      updated(Ready(FileItems(items)))
  }
}

case class FileItems(items: Seq[FileItem]) {
  def updated(items: Seq[FileItem]) = FileItems(items)
}

case class ListAllFiles(path: String) extends Action

case class UpdateListFiles(items: Seq[FileItem]) extends Action
