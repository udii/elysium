package services

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import play.api.Play.current
import models._
import models.Card._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsValue, Json}
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument
import play.api.libs.ws.WS

object CardDao {

  private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("cards")

  def save(message: Card): Future[Card] = {
    println("save "+message)

    collection.save(message).map {
      case ok if ok.ok =>
        message
      case error => throw new RuntimeException(error.message)
    }

    WS.url("http://localhost:9200/"+message.trt+"/"+message.kind+"/"+message._id.stringify).post(message.message).map {
      response =>
        val body = response.json
        val status = (body \ "ok").as[Boolean]
        if (status) {
          message
        } else {
          throw new RuntimeException()
        }
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

  def searchCards(message: Card): Future[Seq[Card]] = {
    println("search "+message.message+" "+message.trt)

    val arr = Json.parse(message.message).as[Map[String,JsValue]].map(
      { case (a,b) => Json.obj(
          "text" -> Json.obj(
            a -> b
          )
      )} ).toList

//    val obj = Json.obj(
//      "query" -> Json.obj(
//        "bool" -> Json.obj(
//          "should" -> arr
//        )
//      )
//    )

    val obj = Json.obj(
      "query" -> Json.obj(
        "fuzzy" ->  Json.parse(message.message)
      )
    )

    println("query: "+obj)
    val query = Json.stringify(obj)
    WS.url("http://localhost:9200/"+message.trt+"/_search").post(query).map {
      response =>
        val body = response.json
        (body \ "hits" \ "hits").validate[List[JsValue]]
          .map[List[Card]]( j => j
          .map( e =>
            {
              println("E "+e)
              println("E "+(e \ "_source").getClass())
              println("E "+Json.stringify(e \ "_source"))
              new Card(new BSONObjectID((e \ "_id").validate[String].get),
                      (e \ "_type").validate[String].get,
                      (e \ "_index").validate[String].get,
                      (e \ "_id").validate[String].get,
                      Json.stringify(e \ "_source")
                      )

            }
          )
          ).get
    }

//    Future { Seq (new Card(new BSONObjectID("1"),"a","b","c","d")) }

  }


}
