/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestpaint;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DCanvasTest {
    
    public DCanvasTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of setFillShape method, of class DCanvas.
     */
    @Test
    public void testSetFillShape(){
        boolean fillShape = false;
        System.out.println("setFillShape");
        DCanvas instance = new DCanvas();
        instance.setFillShape(fillShape);
        assertEquals(fillShape,instance.getFillShape());//if getFillShape returns value defined above, we good
    }

    /**
     * Test of setLineColor method, of class DCanvas.
     */
    @Test
    public void testSetLineColor() {
        System.out.println("setLineColor");
        Color color = Color.MAROON;
        DCanvas instance = new DCanvas();
        instance.setLineColor(color);
        assertEquals(color, instance.getLineColor());   //if getLineColor returns value defined above, we good
    }

    /**
     * Test of setFillColor method, of class DCanvas.
     */
    @Test
    public void testSetFillColor() {
        System.out.println("setFillColor");
        Color color = Color.CHARTREUSE;
        DCanvas instance = new DCanvas();
        instance.setFillColor(color);
        assertEquals(color, instance.getFillColor());   //if getLineColor returns value defined above, we good
    }

    /**
     * Test of setLineWidth method, of class DCanvas.
     */
    @Test
    public void testSetLineWidth() {
        System.out.println("setLineWidth");
        double width = 10.0;
        DCanvas instance = new DCanvas();
        instance.setLineWidth(width);
        //had to add a third param delta
        assertEquals(10.0,instance.getLineWidth(),0.1); //if getLineWidth returns value defined above, we good
    }
}
