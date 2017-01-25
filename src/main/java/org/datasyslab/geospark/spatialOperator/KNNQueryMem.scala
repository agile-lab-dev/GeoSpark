/**
 * FILE: KNNQuery.java
 * PATH: org.datasyslab.geospark.spatialOperator.KNNQuery.java
 * Copyright (c) 2017 Arizona State University Data Systems Lab
 * All rights reserved.
 */
package org.datasyslab.geospark.spatialOperator;

import com.vividsolutions.jts.geom.{_}
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.function.Function
import org.datasyslab.geospark.knnJudgement._
import org.datasyslab.geospark.spatialRDD.LineStringRDD
import org.datasyslab.geospark.spatialRDD.PointRDD
import org.datasyslab.geospark.spatialRDD.PolygonRDD
import org.datasyslab.geospark.spatialRDD.RectangleRDD
import java.io.Serializable
import java.util.ArrayList
import scala.collection.JavaConverters._

import org.datasyslab.geospark.spatialList.LineStringList;


// TODO: Auto-generated Javadoc

/**
 * The Class KNNQuery.
 */
object KNNQueryMem extends Serializable {


	/**
	 * Spatial knn query.
	 *
	 * @param spatialList the spatial RDD
	 * @param queryCenter the query center
	 * @param k the k
	 * @param useIndex the use index
	 * @return the list
	 */
	def SpatialKnnQuery( spatialList: LineStringList, queryCenter: Point , k: Int, useIndex: Boolean): List[LineString] = {
		// For each partation, build a priority queue that holds the topk
		//@SuppressWarnings("serial")
		if(useIndex)
		{
	        if(spatialList.index == null) {
	            throw new NullPointerException("Need to invoke buildIndex() first, indexedCollectionNoId is null");
	        }
					val fact = new GeometryFactory()
					val tmp = KnnJudgementUsingIndexS.invoke(spatialList.index, queryCenter,k)
					val result = tmp.sorted(new GeometryDistanceOrdering(queryCenter)).take(k)
					result.map(r => r.asInstanceOf[LineString]).toList
		}
		else
		{
			val tmp = GeometryKnnJudgementS.invoke(spatialList.rawSpatialCollection.toIterator, queryCenter,k);
			val result: Array[Object] = tmp.sorted(new GeometryDistanceOrdering(queryCenter)).take(k)
			result.map(r => r.asInstanceOf[LineString]).toList
		}
	}

	def SpatialKnnQueryJava( spatialList: LineStringList, queryCenter: Point , k: Int, useIndex: Boolean): java.util.List[LineString] = {
		SpatialKnnQuery(spatialList, queryCenter, k, useIndex).asJava
	}

}
