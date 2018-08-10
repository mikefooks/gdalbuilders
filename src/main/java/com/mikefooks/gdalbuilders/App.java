package com.mikefooks.gdalbuilders;


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
        
        System.exit(0);
    }
}
