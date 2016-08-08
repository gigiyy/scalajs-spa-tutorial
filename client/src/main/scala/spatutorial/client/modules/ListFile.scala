package spatutorial.client.modules

import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import spatutorial.client.components.Bootstrap.Panel
import spatutorial.client.services.{FileItems, ListAllFiles}

/**
  * Created by GiGiYY on 8/8/2016.
  */
object ListFile {

  case class Props(proxy: ModelProxy[Pot[FileItems]])

  case class State(path: String)

  class Backend($: BackendScope[Props, State]) {

    def mounted(props: Props) =
      Callback.when(props.proxy().isEmpty)(props.proxy.dispatch(ListAllFiles("")))

    def change(p:Props)(e: ReactEventI) = {
      val path = e.target.value
      val cb = {
        p.proxy.dispatch(ListAllFiles(path))
      }
      cb >> $.modState(s => State(path))
    }

    def render(p: Props, s: State) =
      Panel(Panel.Props("List server files"),
        <.div(
          <.p("Type the file names to list matched files, add '/' to list sub-directory files"),
          <.input(^.`type` := "text", ^.value := s.path, ^.onChange  ==> change(p)),
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          p.proxy().render(items => {
            val FileList = ReactComponentB[FileItems]("File List")
              .render_P(items =>
                <.ul(
                  for (item <- items.items)
                    yield if (item.size == 0) <.b(<.li(item.name))
                    else <.b(<.li(item.name + " - " + item.size + " bytes"))
                )
              ).build

            FileList(items)
          }
          )
        )
      )
  }

  val component = ReactComponentB[Props]("List File")
    .initialState_P(props => State(""))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[FileItems]]) = component(Props(proxy))
}
