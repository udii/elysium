package controllers

import play.api.mvc._
import play.api.Routes

object MainController extends Controller {

//  def playground() = Action {
//    Ok(views.html.pg())
//  }

  def router = Action { implicit req =>
    Ok(
      Routes.javascriptRouter("routes")(
        routes.javascript.MessageController.getMessages,
        routes.javascript.MessageController.saveMessage,
        routes.javascript.MessageController.deleteMessage
      )
    ).as("text/javascript")
  }
}
