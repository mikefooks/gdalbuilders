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
    private String layerName;
    private FeatureBuilder featureBuilder;
    private int geomType;

    public LayerBuilder (String layerName, int geomType)
    {
        this.layerName = layerName;
        this.geomType = geomType;
        layerDefn = new FeatureDefn();
        features = new LinkedList<>();
        featureBuilder = new FeatureBuilder();
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

    class FeatureBuilder {
        private SpatialReference srs;
        private Feature feature;
        private Geometry geometry;
        private Map<String, Boolean> fulfilled;

        FeatureBuilder ()
        {
            feature = new Feature(layerDefn);
            geometry =  new Geometry(geomType);
            fulfilled = new HashMap<>();

            String[] keys = { "srs", "geometry", "fields" };
            for (String key: keys) {
                fulfilled.put(key, false);
            }
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


