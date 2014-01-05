package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class Trait(_id: BSONObjectID, name:String, message: String)

object Trait {
  implicit val messageFormat = Json.format[Trait]
}