package com.mikefooks.gdalbuilders;


import org.gdal.osr.SpatialReference;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconst;

public class GTiffBuilder
{
    private String dsName;
    private int xSize, ySize;
    private double[] geoTransform;
    private SpatialReference srs;
    private Dataset ds;
    private Driver driver;

    GTiffBuilder (String dsName, int xSize, int ySize)
    {
        this.driver = gdal.GetDriverByName("GTiff");
        this.ds = this.driver.Create(dsName, xSize, ySize, 1,
                                     gdalconst.GDT_Float64);
        this.xSize = xSize;
        this.ySize = ySize;
        this.dsName = dsName;
    }

    GTiffBuilder setDimensions (int xSize, int ySize)
    {
        this.xSize = xSize;
        this.ySize = ySize;
        return this;
    }

    GTiffBuilder setGeoTransform (double[] geoTransform)
    {
        ds.SetGeoTransform(geoTransform);
        return this;
    }

    GTiffBuilder addBand (double[] data)
    {
        Band newBand = ds.GetRasterBand(1);
        newBand.WriteRaster(0, 0, xSize, ySize, data);

        return this;
    }

    GTiffBuilder setBandData (int band, int[] data)
    {
        ds.GetRasterBand(band)
            .WriteRaster(0, 0, xSize, ySize, data);
        return this;
    }

    GTiffBuilder setSpatialReference (SpatialReference srs)
    {
        ds.SetProjection(srs.ExportToWkt());
        return this;
    }

    void build ()
    {
        this.ds.delete();
    }
}
