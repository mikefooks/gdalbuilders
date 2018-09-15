package com.mikefooks.gdalbuilders;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.gdal.osr.SpatialReference;
import org.gdal.ogr.ogr;
import org.gdal.gdal.*;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

public class App 
{
    public static void main (String[] args)
    {
        SpatialReference srs = new SpatialReference();
        srs.SetWellKnownGeogCS("NAD83");
        srs.SetUTM(10, 1);

        /*
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
        
        */
        RealDistribution rd = new NormalDistribution(100.0, 30.0);
        
        double[] geoTransform = { 470216.0, 10.0, 0.0, 5366939.0, 0.0, -10.0 };

        double[] bandOneData = new double[90000];

        for (int i = 0; i < bandOneData.length; i++)
            bandOneData[i] = rd.sample();
        
        Dataset raster = new RasterBuilder()
            .setdsName("/home/mike/gis/data/some_noise.tif")
            .setXYSize(300, 300)
            .setSpatialReference(srs)
            .setGeoTransform(geoTransform)
            .addBand(bandOneData)
            .build();

        raster.delete();
                
        System.exit(0);
    }
}
