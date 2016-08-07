package spatutorial.client.modules

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB, _}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.HTMLElement
import spatutorial.client.components.Bootstrap.Panel

import scala.scalajs.js

/**
  * Created by GiGiYY on 8/7/2016.
  */
object Clock {

  case class State(d: js.Date)

  class Backend($: BackendScope[Unit, State]) {
    var interval: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined
    var canvas: js.UndefOr[html.Canvas] = js.undefined
    var renderer: js.UndefOr[dom.CanvasRenderingContext2D] = js.undefined

    def tick = $.modState(s => State(new js.Date))

    def start = Callback {
      val ref = Ref[HTMLElement]("canvas")
      val c = ref($).asInstanceOf[html.Canvas]
      c.width = c.parentElement.clientWidth
      c.height = c.parentElement.clientHeight
      val r = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
      val g = r.createLinearGradient(
        c.width / 2 - 100, c.height / 2 - 30, c.width / 2 + 100, c.height / 2 - 30
      )
      g.addColorStop(0, "blue")
      g.addColorStop(0.5, "green")
      g.addColorStop(1, "red")
      r.fillStyle = g
      r.textAlign = "center"
      r.textBaseline = "middle"
      r.font = "100px sans-serif"
      canvas = c
      renderer = r

      interval = js.timers.setInterval(88)(tick.runNow())
    }

    def clear = Callback {
      interval foreach js.timers.clearInterval
      interval = js.undefined
      canvas = js.undefined
      renderer = js.undefined
    }

    def update(s: State) = Callback {
      val text = Seq(s.d.getHours(), s.d.getMinutes(), s.d.getSeconds(), s.d.getMilliseconds() / 10)
        .map("%02d".format(_)).mkString(":")
      canvas foreach { c =>
        renderer foreach { r =>
          r.clearRect(0, 0, c.width, c.height)
          r.fillText(text, c.width / 2, c.height / 2)
        }
      }
    }

    def render(s: State) =
      Panel(Panel.Props("A Colorful Digital Clock"),
        <.div(
          <.canvas(
            ^.ref := "canvas",
            ^.height := 300
          )
        )
      )

  }

  private val component = ReactComponentB[Unit]("Clock")
    .initialState(State(new js.Date))
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .componentWillUnmount(_.backend.clear)
    .componentDidUpdate(a => a.component.backend.update(a.currentState))
    .build

  def apply() = component()
}
