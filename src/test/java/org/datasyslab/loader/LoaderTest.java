package org.datasyslab.loader;

import com.vividsolutions.jts.geom.LineString;
import org.datasyslab.geospark.utils.RDDSampleUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by paolo on 25/01/2017.
 */
public class LoaderTest {

    @Test
    public void testParsing() throws Exception {
        String str1 = "(2002; 8307; (; ; ); (1; 2; 1; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ); (7,74409; 45,1; 7,74276; 45,10033; 7,74204; 45,10047; 7,74136; 45,10076; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ))";
        String str2 = "(2002; 8307; (; ; ); (1; 2; 1; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ); (7,74724; 45,09953; 7,74693; 45,09968; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ))";
        String[] fields1 = new CTLLoader(7).parseGeometry(str1);
        String[] fields2 = new CTLLoader(7).parseGeometry(str2);
        System.out.println(fields1.length);
        Integer i = 0;
        for(String field: fields1){
            System.out.println(i + " - " + field);
            i++;
        }

        System.out.println("-----------------------------------------");

        System.out.println(fields2.length);
        i = 0;
        for(String field: fields2){
            System.out.println(i + " - " + field);
            i++;
        }
    }


    @Test
    public void testLineStringBuilding() throws Exception {
        String str1 = "(2002; 8307; (; ; ); (1; 2; 1; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ); (7,74409; 45,1; 7,74276; 45,10033; 7,74204; 45,10047; 7,74136; 45,10076; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ))";
        String str2 = "(2002; 8307; (; ; ); (1; 2; 1; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ); (7,74724; 45,09953; 7,74693; 45,09968; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ; ))";
        LineString line1 = new CTLLoader(7).buildLineString(str1);
        LineString line2 = new CTLLoader(7).buildLineString(str2);

        System.out.println(line1.toString());
        System.out.println(line2.toString());
    }
}
