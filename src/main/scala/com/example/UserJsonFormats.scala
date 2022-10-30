package com.example

//#json-formats
import spray.json.DefaultJsonProtocol

object UserJsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(UserRegistry.ActionPerformed)

}
//#json-formats
