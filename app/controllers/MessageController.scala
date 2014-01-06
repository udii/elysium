package controllers

import scala.concurrent._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import models._
import services.TraitDao
import reactivemongo.bson.BSONObjectID

object MessageController extends Controller {

  def getMessages() = Action.async { implicit req =>
      for {
        messages <- TraitDao.findAll(0, 100)
      } yield {
        Ok(Json.toJson(messages))
      }
  }

  case class MessageForm(id:String,kind:String, name:String, message: String) {
    def toTrait: Trait = {
      Trait(id match { case "-1" => BSONObjectID.generate case _ => BSONObjectID(id) }, kind, name, message)
    }
  }
  
  case class IDForm(id:String) {
    def toID: ID = new ID(id)
  }

  implicit val messageFormFormat = Json.format[MessageForm]

  implicit val idFormFormat = Json.format[IDForm]

  def saveMessage = Action.async(parse.json) { req =>
    Json.fromJson[MessageForm](req.body).fold(
      invalid => Future(BadRequest("Bad message form")),
      form => {
        TraitDao.save(form.toTrait).map(_ => Created)
      }
    )
  }

  def deleteMessage = Action.async(parse.json) { req =>
    Json.fromJson[IDForm](req.body).fold(
      invalid => Future(BadRequest("Bad message form")),
      form =>  {
        TraitDao.delete(new BSONObjectID(form.toID.id)).map(_ => Ok)
      }
    )
  }
}