package com.mikefooks.gdalbuilders;


import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

import org.gdal.ogr.ogr;

public class LayerBuilderTest
{
    private LayerBuilder layerBuilder;

    @Before
    public void createLayerBuilder ()
    {
        layerBuilder = new LayerBuilder("testLayer", ogr.wkbPoint);
    }

    @After
    public void destroyLayerBuilder ()
    {
        layerBuilder = null;
    }

    @Test
    public void constructor ()
    {
        Assert.assertEquals(LayerBuilder.class, layerBuilder.getClass());
    }

    @Test
    public void getLayerName ()
    {
        Assert.assertEquals("testLayer", layerBuilder.getLayerName());
    }

    @Test
    public void getGeomType ()
    {
        Assert.assertEquals(ogr.wkbPoint, layerBuilder.getGeomType());        
    }
}
