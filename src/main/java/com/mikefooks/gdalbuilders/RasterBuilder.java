package com.mikefooks.gdalbuilders;


import java.util.List;
import java.util.ArrayList;

import org.gdal.osr.SpatialReference;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconst;

public class RasterBuilder
{
    private String dsName;
    private int xSize, ySize;
    private double[] geoTransform;
    private SpatialReference srs;
    private List<double[]> bandData;

    public RasterBuilder ()
    {
        bandData = new ArrayList<>();
    }

    public RasterBuilder setdsName (String dsName)
    {
        this.dsName = dsName;
        return this;
    }

    public RasterBuilder setXYSize (int xSize, int ySize)
    {
        this.xSize = xSize;
        this.ySize = ySize;
        return this;
    }

    public RasterBuilder setGeoTransform (double[] geoTransform)
    {
        this.geoTransform = geoTransform;
        return this;
    }

    public RasterBuilder setSpatialReference (SpatialReference srs)
    {
        this.srs = srs;
        return this;
    }

    public RasterBuilder addBand (double[] data)
    {
        bandData.add(data);
        return this;
    }

    /* TODO: make this work in projections other than UTM */
    public double[] getCoordinates (int band, int xPix, int yPix)
    {
        if (xPix >= xSize || yPix >= ySize) {
            throw new IllegalArgumentException("specified pixels out of range.");
        }

        return new double[] { 2.3, 5.6 };
    }

    public Dataset build ()
    {
        gdal.AllRegister();
        Driver driver = gdal.GetDriverByName("GTiff");
        Dataset rasterOut = driver.Create(dsName,
                                          xSize, ySize,
                                          bandData.size(),
                                          gdalconst.GDT_Float64);
        rasterOut.SetProjection(srs.ExportToWkt());
        rasterOut.SetGeoTransform(geoTransform);

        for (int i = 0; i < bandData.size(); i++) {
            Band newBand = rasterOut.GetRasterBand(i + 1);
            newBand.WriteRaster(0, 0, xSize, ySize, bandData.get(i));
        }

        return rasterOut;
    }    
}
