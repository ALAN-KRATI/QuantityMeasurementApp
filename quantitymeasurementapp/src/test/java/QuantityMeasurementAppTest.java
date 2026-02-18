import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class QuantityMeasurementAppTest {
    @Test
    void testEquality_SameValue(){
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f2));
    }

    @Test
    void testEquality_DifferentValue(){
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(2.0);

        assertFalse(f1.equals(f2));
    }

    @Test
    void testEquality_NullComparison(){
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        
        assertFalse(f1.equals(null));
    }

    @Test
    void testEquality_NonNumericInput() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        String f2 = "1.0";

        assertFalse(f1.equals(f2));
    }

    @Test
    void testEquality_SameReference(){
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(f1.equals(f1));
    }

    @Test
    void testInchesEquality_SameValue(){
        assertTrue(QuantityMeasurementApp.compareInches(1.0, 1.0));
    }

    @Test
    void testInchesEquality_DifferentValue(){
        assertFalse(QuantityMeasurementApp.compareInches(1.0, 2.0));
    }

    @Test
    void testInchesEquality_NullComparison(){
        QuantityMeasurementApp.Inches inch = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(inch.equals(null));
    }

    @Test 
    void testInchesEquality_NonNumericInput(){
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        String i2 = "1.0";
        assertFalse(i1.equals(i2));
    }

    @Test
    void testInchesEquality_SameReference(){
        QuantityMeasurementApp.Inches i = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(i.equals(i));
    }


}