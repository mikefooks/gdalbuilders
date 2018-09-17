package com.mikefooks.gdalbuilders;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.function.Consumer;

import org.gdal.osr.SpatialReference;
import org.gdal.ogr.ogr;
import org.gdal.gdal.*;

import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

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
        */

        double[] bounds = { 470903.0, 478151.0, 5361472.0, 5366408.0 };
        RandomAreas ra = new RandomAreas(bounds, 10, 12);

        String dsName = "/home/mike/gis/data/random_points";
        LayerBuilder lb = new LayerBuilder("random_points2", ogr.wkbPoint);

        for (Node node: ra.getParentNodes()) {
            List<Double[]> pointList = new ArrayList<>();
            pointList.add(new Double[] { node.getX(), node.getY() });
            lb.addFeature(new HashMap<>(), pointList);
        }
        
        lb.writeOut(dsName, srs);
                
        System.exit(0);
    }
}

class RandomAreas
{
    private RealDistribution parentDist, childrenDist;
    private int nParents, kChildren;
    private double width, height;
    private double[] bounds;
    private List<Node> parentNodes;
    private List<Double[]> childNodes;

    /**
     * order of bounds array is x0, x1, y0 (bottom), y1 (top).
     */
    public RandomAreas (double[] bounds, int nParents, int kChildren)
    {
        this.bounds = bounds;
        this.nParents = nParents;
        this.kChildren = kChildren;

        this.width = bounds[1] - bounds[0];
        this.height = bounds[3] - bounds[2];
        
        parentNodes = new ArrayList<>();

        parentDist = new UniformRealDistribution();
        childrenDist = new NormalDistribution();

        _createParentNodes();
    }

    private void _createParentNodes ()
    {
        for (int i = 0; i < nParents; i++)
            parentNodes.add(new Node(_generateParentXSample(),
                                     _generateParentYSample()));
    }

    private Double _generateParentXSample ()
    {
        return (parentDist.sample() * width) + bounds[0];
    }

    private Double _generateParentYSample ()
    {
        return (parentDist.sample() * height) + bounds[2];
    }

    List<Node> getParentNodes ()
    {
        return parentNodes;
    }

    public String toString ()
    {
        String ret = "";
        for (Node node: parentNodes)
            ret += String.format("%f, %f\n", node.getX(), node.getY());
        
        return ret;
    }
}

class Node
{
    private Node parent;
    private double x, y;
    private List<Node> children;
    private String[] attrNames;
    private Object[] attrValues;
    
    public Node (double x, double y)
    {
        this.x = x;
        this.y = y;
        children = new ArrayList<>();
    }

    public boolean hasChildren ()
    {
        return children.size() > 0;
    }

    public double getX ()
    {
        return x;
    }

    public double getY ()
    {
        return y;
    }

    public void addChild (Node child)
    {
        children.add(child);
    }        
}
