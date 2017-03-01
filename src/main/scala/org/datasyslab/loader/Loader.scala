package org.datasyslab.loader

import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory
import org.datasyslab.geospark.enums.IndexType
import org.datasyslab.geospark.spatialList.{GeometryList, GzippedStreet, Street, StreetType}

import scala.io.Source
import scala.util.Try

/**
  * Created by paolo on 25/01/2017.
  */
trait Loader {

  def loadGeometry(source: String): Iterator[(Array[String],Geometry)]

}

object CTLLoader{

  //Pay attention to side effects

  var index: GeometryList[GzippedStreet] = null
  def getStreetIndex(path: String) = {
    if (index == null){
      index = new CTLLoader(7).load(path)
    }
    index
  }

}

class CTLLoader(geometryPosition: Int) extends Loader{

  val separator = """\|\|"""
  val geoSeparator = ';'
  val openStep = '('
  val closeStep = ')'

  val sridFactory8003 = new GeometryFactory(new PrecisionModel(), 8003, CoordinateArraySequenceFactory.instance())

  def load(source: String): GeometryList[Street] = {
    var i=0
    val streetList: Iterator[Street] = loadGeometry(source).map(e => {

      if(i % 10000 == 0){
        println("loaded "+i+" lines")
      }

      val lr: LineString = e._2.asInstanceOf[LineString]
      val fields = e._1
      val streetType: String = fields(4)
      val st = streetType match {
        case "\"1\"" => StreetType.Motorway
        case "\"2\"" => StreetType.ExtraUrban
        case "\"3\"" => StreetType.Area1_Large
        case "\"4\"" => StreetType.Area2_Medium
        case "\"5\"" => StreetType.Area3_Small
        case _ => StreetType.Unknown
      }

      val length: Double = fields(8).replace(',', '.').toDouble
      var bidirected: Boolean = false
      if (fields(10) == "\"Y\"") bidirected = true
      val street: String = fields(11)
      val city: String = fields(12)
      val county: String = fields(13)
      val state: String = fields(14)
      val country: String = fields(15)
      val fromSpeed: Integer = fields(16).toInt
      val toSpeed: Integer = fields(17).toInt
      i += 1

      Street(lr, street, city, county, state, country, Math.max(fromSpeed, toSpeed), bidirected, length, st)

    })

    val streetL = streetList.toList
    println("starting to build index")
    val streetIndex = new GeometryList[Street](streetL)
    streetIndex.buildIndex(IndexType.RTREE)
    println("index built")
    streetIndex
  }

  override def loadGeometry(source: String): Iterator[(Array[String],Geometry)] = {

    var data = false
    val reader = Source.fromFile(source,"UTF-8")


    reader.getLines.flatMap(line => {
        val ls: Option[(Array[String], Geometry)] = if (!data) {
          data = if (line == "BEGINDATA") true else false
          Option.empty[(Array[String],Geometry)]
        } else {

            val fields: Array[String] = line.split(separator)
            if(fields.size == 0){
              println("bad splitting")
              Option.empty[(Array[String],LineString)]
            }else {
              val geometry = fields(geometryPosition)
              val lineString = buildGeometry(geometry)
              lineString.map(ls => (fields, ls))
            }
        }
        ls
      })
  }

  def buildGeometry(geoStr: String): Option[Geometry] = {
    val fields = parseGeometry(geoStr)
    if(fields.size == 205){
      Some(buildGeometry(fields))
    }else{
      println(fields.size)
      None
    }
  }

  def buildGeometry(fields: Array[String]): Geometry = {
    val geotype = fields(0)
    if(geotype == "2002") {
      val lonlat = fields.slice(105, fields.length)
      val lonlatGood = lonlat.filterNot(_.isEmpty)
      val coordinates: Iterator[Coordinate] = lonlatGood.sliding(2, 2).map(ll => new Coordinate(ll(0).replace(',', '.').toDouble, ll(1).replace(',', '.').toDouble))

      sridFactory8003.createLineString(coordinates.toArray)
    }else{
      throw new NotImplementedError("Only lines are handled")
    }
  }

  def parseGeometry(geoStr: String): Array[String] = {
    if(geoStr.head == openStep){
      parseGeometry(geoStr.tail)
    }else{
      val cleanGeoStr = if(geoStr.head == closeStep) geoStr.tail else geoStr
      val sepPos = cleanGeoStr.indexOf(geoSeparator)
      if(sepPos == -1) {
        val closePos = cleanGeoStr.indexOf(closeStep)
        if (closePos != -1) {
          val newField = cleanGeoStr.substring(0, closePos)
          Array(newField)
        }else{
          Array.empty[String]
        }
      }
      else {
        val newField = cleanGeoStr.substring(0, sepPos)
        val otherPart = cleanGeoStr.substring(sepPos + 2)
        parseGeometry(otherPart).+:(newField)
      }
    }

  }

  def parseGeoField(geoStr: String) = {
      val geoField = geoStr.substring(1,geoStr.size-2)
      //val geoFields = geoField.split(geoSeparator,-1)
  }
}
