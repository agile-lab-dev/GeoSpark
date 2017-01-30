/**
 * FILE: LineStringRDD.java
 * PATH: org.datasyslab.geospark.spatialRDD.LineStringRDD.java
 * Copyright (c) 2017 Arizona State University Data Systems Lab
 * All rights reserved.
 */
package org.datasyslab.geospark.spatialList

import java.io.FileWriter
import java.util.ArrayList

import com.vividsolutions.jts.geom._
import org.datasyslab.geospark.spatialList.StreetType.StreetType
import org.wololo.jts2geojson.GeoJSONWriter

// TODO: Auto-generated Javadoc


object StreetType extends Enumeration {
    type StreetType = Value
    val Motorway, ExtraUrban, Area1_Large, Area2_Medium, Area3_Small, Unknown = Value
}


case class Street(lineString: LineString, street: String, city: String, county: String, state: String, country: String, speedLimit: Int, bidirected: Boolean, length: Double, streetType: StreetType) extends Geometry(lineString.getFactory) {

    override def toString() = {
        s"""Line:${lineString.toString()}
           |Street:${street}
           |City:${city}
           |County:${county}
           |State:${state}
           |Country:${country}
           |FromSpeed:${speedLimit}
           |Bidirected:${bidirected}
           |Length:${length}
           |StreetType:${streetType}
         """.stripMargin
    }

    /** As seen from class Street, the missing signatures are as follows.
      *  For convenience, these are usable as stub implementations.
      */
    def apply(filter: CoordinateFilter) = lineString.apply(filter)

    def apply(filter: CoordinateSequenceFilter)  = lineString.apply(filter)

    def apply(filter: GeometryFilter) = lineString.apply(filter)

    def apply(filter: GeometryComponentFilter) = lineString.apply(filter)

    override def computeEnvelopeInternal(): Envelope = {
        if (isEmpty)
            new Envelope
        else
            lineString.getCoordinateSequence().expandEnvelope(new Envelope)

    }

    override def getBoundary: Geometry = lineString.getBoundary

    override def compareToSameClass(o: scala.Any): Int = {
        val s: Street = o.asInstanceOf[Street]
        // MD - optimized implementation
        var i: Int = 0
        var j: Int = 0
        while (i < getNumPoints && j < s.getNumPoints) {
            val comparison: Int = lineString.getCoordinateSequence().getCoordinate(i).compareTo(s.lineString.getCoordinateSequence.getCoordinate(j))
            if (comparison != 0) return comparison
            i += 1
            j += 1
        }
        if (i < getNumPoints) {
            return 1
        }
        if (j < s.getNumPoints) {
            return -1
        }
        return 0
    }

    override def compareToSameClass(o: scala.Any, comp: CoordinateSequenceComparator): Int = {
        val s: Street = o.asInstanceOf[Street]
        return comp.compare(lineString.getCoordinateSequence(), s.lineString.getCoordinateSequence())
    }

    override def getCoordinates: Array[Coordinate] = lineString.getCoordinates

    override def getDimension: Int = lineString.getDimension

    override def getGeometryType: String = lineString.getGeometryType

    override def getBoundaryDimension: Int = lineString.getBoundaryDimension

    override def getCoordinate: Coordinate = lineString.getCoordinate

    override def isEmpty: Boolean = lineString.isEmpty

    override def normalize(): Unit = lineString.normalize()

    override def reverse(): Geometry = lineString.reverse()

    override def equalsExact(other: Geometry, tolerance: Double): Boolean = lineString.equalsExact(other, tolerance)

    override def getNumPoints: Int = lineString.getNumPoints
}

/**
 * The Class LineStringRDD.
 */
class StreetList(polygonList: List[LineString]) extends SpatialList{

    def this(lineList: ArrayList[Geometry]) = this( lineList.toArray().map(s => s.asInstanceOf[LineString]).toList )

		this.rawSpatialCollection = polygonList
    this.boundary();
    this.totalNumberOfRecords = this.rawSpatialCollection.size;




    /**
     * Save as geo JSON.
     *
     * @param outputLocation the output location
     */
    def saveAsGeoJSON(outputLocation: String ) {
        val writer = new GeoJSONWriter();
        val fw = new FileWriter(outputLocation, true)
        try{
        this.rawSpatialCollection.foreach(spatialObject => {
            val json = writer.write(spatialObject.asInstanceOf[Geometry])
            val jsonstring = json.toString()
            fw.write(jsonstring)
        })}
        finally fw.close()
    }
    
    /**
     * Minimum bounding rectangle.
     *
     * @return the rectangle RDD
     */
    def MinimumBoundingRectangle(): RectangleList = {
        val rectangleList = this.rawSpatialCollection.map(spatialObject => {
            spatialObject.asInstanceOf[Geometry].getEnvelopeInternal()
        })
        new RectangleList(rectangleList);
    }
}
