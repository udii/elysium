package controllers

import scala.concurrent._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import models._
import services.TraitDao
import services.CardDao
import reactivemongo.bson.BSONObjectID

object CardController extends Controller {


  def getCards = Action.async(parse.json) { req =>
    println("GET "+req.body)
    Json.fromJson[CardForm](req.body).fold(
      invalid => Future(BadRequest("Bad message form")),
      form => {
        println(form)
        for {
          messages <- CardDao.findCards(form.trt)
        } yield {
          println("GET "+messages)
          Ok(Json.toJson(messages))
        }
      }
    )
  }

  case class CardForm(id:String, kind:String, trt:String, name:String, message: String) {
    def toCard: Card = {
      Card(id match { case "-1" => BSONObjectID.generate case _ => BSONObjectID(id) }, kind, trt, name, message)
    }
  }
  
  case class IDForm(id:String) {
    def toID: ID = new ID(id)
  }

  implicit val cardFormFormat = Json.format[CardForm]

  implicit val idFormFormat = Json.format[IDForm]

  def saveCard = Action.async(parse.json) { req =>
    println("save "+req.body)
    Json.fromJson[CardForm](req.body).fold(
      invalid => Future(BadRequest("Bad message form")),
      form => {
        println("save "+form)
        CardDao.save(form.toCard).map(_ => Created)
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