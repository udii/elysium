package services

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import play.api.Play.current
import models._
import models.Trait._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument

object TraitDao {

  private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("traits")

  def save(message: Trait): Future[Trait] = {
    collection.save(message).map {
      case ok if ok.ok =>
        //TraitDao.publish("message", message)
        message
      case error => throw new RuntimeException(error.message)
    }
  }

  def delete(id: BSONObjectID) = {
    collection.remove(BSONDocument("_id" -> id)).map {
      case ok if ok.ok => 
        //TraitDao.publish("message", message)
      case error => throw new RuntimeException(error.message)
    }
  }

  def findAll(page: Int, perPage: Int): Future[Seq[Trait]] = {

    collection.find(Json.obj())
      .options(QueryOpts(skipN = page * perPage))
    //   .sort(Json.obj("_id" -> -1))
      .cursor[Trait]
      .collect[List]()
  }

  /** The total number of messages */
  def count: Future[Int] = {
    ReactiveMongoPlugin.db.command(Count(collection.name))
  }

}
