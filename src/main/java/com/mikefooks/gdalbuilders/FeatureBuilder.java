package com.mikefooks.gdalbuilders;


import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.gdal.ogr.*;

class FeatureBuilder {
    private Feature feature;
    private Geometry geometry;

    FeatureBuilder (FeatureDefn defn, int geomType)
    {
        feature = new Feature(defn);
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

    void setFields (Map<String, Object> fields)
    {
        fields.forEach(this::setField);
    }

    void setPoint (Double[] point)
    {
        geometry.AddPoint(point[0], point[1]);
    }

    void setPoints (List<Double[]> points)
    {
        points.forEach(this::setPoint);
    }
        
    Feature build ()
    {
        feature.SetGeometry(geometry);
        return feature;
    }
}
