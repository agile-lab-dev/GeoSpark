/**
 * FILE: RectangleRDD.java
 * PATH: org.datasyslab.geospark.spatialRDD.RectangleRDD.java
 * Copyright (c) 2017 Arizona State University Data Systems Lab
 * All rights reserved.
 */
package org.datasyslab.geospark.spatialList;

import com.vividsolutions.jts.geom.Envelope;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.datasyslab.geospark.enums.FileDataSplitter;
import org.datasyslab.geospark.formatMapper.RectangleFormatMapper;
import org.datasyslab.geospark.spatialRDD.SpatialRDD;


// TODO: Auto-generated Javadoc

/**
 * The Class RectangleRDD.
 */

class RectangleList(rawData: List[Envelope]) extends SpatialList {


	this.rawSpatialCollection = rawData
	this.boundary();
	this.totalNumberOfRecords = this.rawSpatialCollection.size

}
