package com.mikefooks.gdalbuilders;


import java.util.List;
import java.util.LinkedList;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

public class LayerBuilder
{
    private FeatureDefn layerDefn;
    private List<Feature> features;
    private String layerName;
    private int geomType;

    public LayerBuilder (String layerName)
    {
        this.layerName = layerName;
        layerDefn = new FeatureDefn();
        features = new LinkedList<>();
    }

    public LayerBuilder setGeomType (int geomType)
    {
        this.geomType = geomType;
        return this;
    }
    
    public LayerBuilder addField (String name, int fieldType)
    {
        FieldDefn newField = new FieldDefn(name, fieldType);
        layerDefn.AddFieldDefn(newField);
        return this;
    }

    public Layer build (String fileName, SpatialReference srs)
    {
        ShpBuilder shp = new ShpBuilder(fileName, layerName, srs);
        return shp.getLayer();
    }

    public FeatureBuilder getFeatureBuilder () {
        return new FeatureBuilder(geomType, layerDefn);
    }
}

class ShpBuilder {
    private Driver driver;
    private DataSource ds;
    private Layer layerOut;
    private final String driverName = "ESRI Shapefile";

    ShpBuilder (String fileName,
                String layerName,
                SpatialReference srs)
    {
        ogr.RegisterAll();
        driver = ogr.GetDriverByName(driverName);
        ds = driver.CreateDataSource(fileName);
        layerOut = ds.CreateLayer(layerName, srs);
    }

    Layer getLayer ()
    {
        return layerOut;
    }
            
    void release () {
        ds.delete();
    }
}

class FeatureBuilder {
    private SpatialReference srs;
    private Feature feature;
    private Geometry geometry;

    FeatureBuilder (int geomType, FeatureDefn featureDefn)
    {
        feature = new Feature(featureDefn);
        geometry =  new Geometry(geomType);
    }

    void checkType (Object thing)
    {
        System.out.println(thing.getClass());
    }

    FeatureBuilder setField (String fieldName, Object value)
    {
        Class valClass = value.getClass();
        
        if ( valClass == String.class) {
            feature.SetField(fieldName, (String) value);
        } else if (valClass == Double.class) {
            feature.SetField(fieldName, (Double) value);
        }

        return this;
    }        

    Geometry getGeometry ()
    {
        return geometry;
    }

    Feature build ()
    {
        feature.SetGeometry(geometry);
        return feature;
    }
}
