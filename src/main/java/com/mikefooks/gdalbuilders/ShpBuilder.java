package com.mikefooks.gdalbuilders;


import java.util.List;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

class ShpBuilder {
    private Driver driver;
    private DataSource ds;
    private Layer layerOut;
    private final String driverName = "ESRI Shapefile";

    ShpBuilder (String fileName,
                String layerName,
                List<Feature> features,
                List<FieldDefn> fields,
                SpatialReference srs)
    {
        ogr.RegisterAll();
        driver = ogr.GetDriverByName(driverName);
        ds = driver.CreateDataSource(fileName);
        layerOut = ds.CreateLayer(layerName, srs);
        fields.forEach((field) -> layerOut.CreateField(field));
        features.forEach((feature) -> layerOut.CreateFeature(feature));
    }

    Layer getLayer ()
    {
        return layerOut;
    }
            
    void syncAndRelease () {
        ds.SyncToDisk();
        ds.delete();
    }
}
