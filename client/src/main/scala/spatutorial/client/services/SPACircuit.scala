package spatutorial.client.services

import diode._
import diode.data._
import diode.react.ReactConnector

// The base model of our application
case class RootModel(todos: Pot[Todos], motd: Pot[String], fileItems: Pot[FileItems])

// Application circuit
object SPACircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  // initial application model
  override protected def initialModel = RootModel(Empty, Empty, Empty)
  // combine all handlers into one
  override protected val actionHandler = composeHandlers(
    new TodoHandler(zoomRW(_.todos)((m, v) => m.copy(todos = v))),
    new MotdHandler(zoomRW(_.motd)((m, v) => m.copy(motd = v))),
    new ListFileHandler(zoomRW(_.fileItems)((m, v) => m.copy(fileItems = v)))
  )
}