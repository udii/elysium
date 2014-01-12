package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class Card(_id: BSONObjectID, kind:String, trt:String, name:String, message: String)

object Card {
  implicit val messageFormat = Json.format[Card]
}