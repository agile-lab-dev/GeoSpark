package org.datasyslab.loader

import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory

import scala.io.Source
import scala.util.Try

/**
  * Created by paolo on 25/01/2017.
  */
trait Loader {

  def load(source: String): Iterator[(Array[String],LineString)]

}

class CTLLoader(geometryPosition: Int) extends Loader{

  val separator = """\|\|"""
  val geoSeparator = ';'
  val openStep = '('
  val closeStep = ')'

  val sridFactory8003 = new GeometryFactory(new PrecisionModel(), 8003, CoordinateArraySequenceFactory.instance())

  override def load(source: String): Iterator[(Array[String],LineString)] = {

    var data = false
    val reader = Source.fromFile(source,"UTF-8")


    reader.getLines.flatMap(line => {
        val ls: Option[(Array[String], LineString)] = if (!data) {
          data = if (line == "BEGINDATA") true else false
          Option.empty[(Array[String],LineString)]
        } else {

            val fields: Array[String] = line.split(separator)
            if(fields.size == 0){
              println("bad splitting")
              Option.empty[(Array[String],LineString)]
            }else {
              val geometry = fields(geometryPosition)
              val lineString = buildLineString(geometry)
              lineString.map(ls => (fields, ls))
            }
        }
        ls
      })
  }

  def buildLineString(geoStr: String): Option[LineString] = {
    val fields = parseGeometry(geoStr)
    if(fields.size == 205){
      Some(buildLineString(fields))
    }else{
      println(fields.size)
      None
    }
  }

  def buildLineString(fields: Array[String]): LineString = {
    val geotype = fields(0)
    val lonlat = fields.slice(105, fields.length)
    val lonlatGood = lonlat.filterNot(_.isEmpty)
    val coordinates: Iterator[Coordinate] = lonlatGood.sliding(2,2).map(ll => new Coordinate(ll(0).replace(',','.').toDouble, ll(1).replace(',','.').toDouble))

    sridFactory8003.createLineString(coordinates.toArray)

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
