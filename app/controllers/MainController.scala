package controllers

import play.api.mvc._
import play.api.Routes
import controllers.routes

object MainController extends Controller {

//  def playground() = Action {
//    Ok(views.html.pg())
//  }

  def router = Action { implicit req =>
    Ok(
      Routes.javascriptRouter("routes")(
        routes.javascript.MessageController.getMessages,
        routes.javascript.MessageController.saveMessage,
        routes.javascript.MessageController.deleteMessage,
        routes.javascript.CardController.getCards,
        routes.javascript.CardController.searchCards,
        routes.javascript.CardController.saveCard
      )
    ).as("text/javascript")
  }
}
