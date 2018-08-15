package com.mikefooks.gdalbuilders;


import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

public class LayerBuilder
{
    private FeatureDefn layerDefn;
    private List<Feature> features;
    private List<FieldDefn> fields;
    private String layerName;
    private int geomType;

    public LayerBuilder (String layerName, int geomType)
    {
        this.layerName = layerName;
        this.geomType = geomType;
        layerDefn = new FeatureDefn();
        features = new LinkedList<>();
        fields = new LinkedList<>();
    }

    public LayerBuilder addField (String name, Integer dataType)
    {
        FieldDefn newField = new FieldDefn(name, dataType);
        layerDefn.AddFieldDefn(newField);
        fields.add(newField);
        return this;
    }

    public boolean addFeature (Map<String, Object> fieldValues,
                               List<Double[]> points)
    {
        FeatureBuilder builder = new FeatureBuilder();
        fieldValues.forEach((String name, Object value) ->
                            builder.setField(name, value));
        points.forEach((Double[] point) -> builder.addPoint(point));
        builder.build();
        
        return true;
    }

    public void writeOut (String fileName, SpatialReference srs)
    {
        ShpBuilder shp = new ShpBuilder(fileName,
                                        layerName,
                                        features,
                                        fields,
                                        srs);
        shp.syncAndRelease();
    }

    class FeatureBuilder {
        private Feature feature;
        private Geometry geometry;

        FeatureBuilder ()
        {
            feature = new Feature(layerDefn);
            geometry =  new Geometry(geomType);
        }

        void setField (String fieldName, Object value)
        {
            Class valClass = value.getClass();
        
            if ( valClass == String.class) {
                feature.SetField(fieldName, (String) value);
            } else if (valClass == Double.class) {
                feature.SetField(fieldName, (Double) value);
            }
        }

        void addPoint (Double[] point)
        {
            geometry.AddPoint(point[0], point[1]);
        }
        
        void build ()
        {
            feature.SetGeometry(geometry);
            features.add(feature);
        }
    }
}

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
