package services

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import play.api.Play.current
import models._
import models.Card._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument

object CardDao {

  private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("cards")

  def save(message: Card): Future[Card] = {
    println("save "+message)

    collection.save(message).map {
      case ok if ok.ok =>
        message
      case error => throw new RuntimeException(error.message)
    }
  }

  def delete(id: BSONObjectID) = {
    collection.remove(BSONDocument("_id" -> id)).map {
      case ok if ok.ok => 
      case error => throw new RuntimeException(error.message)
    }
  }

  def findCards(id: String): Future[Seq[Card]] = {
    println("find "+id)

    collection.find(BSONDocument("trt" -> id))
      .cursor[Card]
      .collect[List]()
  }

  /** The total number of messages */
  def count: Future[Int] = {
    ReactiveMongoPlugin.db.command(Count(collection.name))
  }

}
