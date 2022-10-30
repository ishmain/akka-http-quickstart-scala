package com.example

//#json-formats
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object QuadEqJsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val quadEqQueryJsonFormat: RootJsonFormat[QuadEqQuery] = jsonFormat3(QuadEqQuery)
  implicit val quadEqSolutionJsonFormat: RootJsonFormat[QuadEqActor.CalculationResponse] =
    jsonFormat1(QuadEqActor.CalculationResponse)
}
//#json-formats
