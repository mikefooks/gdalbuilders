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

    public LayerBuilder ()
    {
        layerDefn = new FeatureDefn();
        features = new LinkedList<>();
        fields = new LinkedList<>();
    }

    public LayerBuilder (String layerName, int geomType)
    {
        this();
        this.layerName = layerName;
        this.geomType = geomType;
    }

    public LayerBuilder setGeomType (int geomType)
    {
        this.geomType = geomType;
        return this;
    }

    public LayerBuilder setLayerName (String layerName)
    {
        this.layerName = layerName;
        return this;
    }
    
    public int getGeomType ()
    {
        return geomType;
    }

    public String getLayerName ()
    {
        return layerName;
    }

    public LayerBuilder addField (String name, Integer dataType)
    {
        FieldDefn newField = new FieldDefn(name, dataType);
        layerDefn.AddFieldDefn(newField);
        fields.add(newField);
        return this;
    }

    public List<FieldDefn> getFields ()
    {
        return fields;
    }

    public void addFeature (Map<String, Object> fieldValues,
                            List<Double[]> points)
    {
        FeatureBuilder builder = new FeatureBuilder(layerDefn, geomType);
        builder.setFields(fieldValues);
        builder.setPoints(points);
        features.add(builder.build());
    }

    public void addFeature (FeatureBuilder fb)
    {
        features.add(fb.build());
    }

    public FeatureBuilder getFeatureBuilder ()
    {
        return new FeatureBuilder(layerDefn, geomType);
    }

    public List<Feature> getFeatures ()
    {
        return features;
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
}
