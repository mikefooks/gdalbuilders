package com.mikefooks.gdalbuilders;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.gdal.osr.SpatialReference;
import org.gdal.ogr.ogr;

public class App 
{
    public static void main (String[] args)
    {
        SpatialReference srs = new SpatialReference();
        srs.SetWellKnownGeogCS("NAD83");
        srs.SetUTM(10, 1);

        LayerBuilder layer = new LayerBuilder("some_points", ogr.wkbPoint)
            .addField("name", ogr.OFTString)
            .addField("magnitude", ogr.OFTReal);

        Map<String, Object> values = new HashMap<>();
        values.put("name", "library");
        values.put("magnitude", 23.0);
        
        List<Double[]> location = new ArrayList<>();
        location.add(new Double[] { 473077.0, 5363359.0 });

        layer.addFeature(values, location);
        layer.writeOut("/home/mike/gis/data/one_point", srs);
        
        System.exit(0);
    }
}
