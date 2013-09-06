package controllers

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import models._
import services.MessageDao
import reactivemongo.bson.BSONObjectID

object MessageController extends Controller {

  /** Action to get the messages */
  def getMessages(page: Int, perPage: Int) = Action { implicit req =>
    Async {
      for {
        count <- MessageDao.count
        messages <- MessageDao.findAll(page, perPage)
      } yield {
        val result = Ok(Json.toJson(messages))
        val next = if (count > (page + 1) * perPage) Some("next" -> (page + 1)) else None
        val prev = if (page > 0) Some("prev" -> (page - 1)) else None
        val links = next ++ prev
        if (links.isEmpty) {
          result
        } else {
          result.withHeaders("Link" -> links.map {
            case (rel, p) =>
              "<" + routes.MessageController.getMessages(p, perPage).absoluteURL() + ">; rel=\"" + rel + "\""
          }.mkString(", "))
        }
      }
    }
  }

  /** Action to save a message */
  def saveMessage = Action(parse.json) { req =>
    val message = Message(BSONObjectID.generate, (req.body \ "message").as[String])
    Async {
      MessageDao.save(message).map(_ => Created)
    }
  }

}