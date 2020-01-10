package demo

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import react.semanticui.elements.image._

object Imag {

  private val component =
    ScalaComponent
      .builder[Unit]("Form")
      .render { _ =>
        <.div(
          ^.cls := "centered",
          <.div(
            Image(src       = "http://www.gemini.edu/images/pio/News/2019/wf2019_02/fig1.jpg",
                  className = "non-draggable")
          )
        )
      }
      .build

  def apply() = component()
}
