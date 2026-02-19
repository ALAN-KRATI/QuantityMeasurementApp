import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
    private static final double EPSILON = 1e-6;

   @Test
    void testConversion_FeetToInches(){
        assertEquals(12.0, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

   @Test
    void testConversion_InchesToFeet(){
        assertEquals(2.0, QuantityLength.convert(24.0, LengthUnit.INCH, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_YardsToInches(){
        assertEquals(36.0, QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToYards(){
        assertEquals(2.0, QuantityLength.convert(72.0, LengthUnit.INCH, LengthUnit.YARDS), EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches(){
        assertEquals(1.0, QuantityLength.convert(2.54, LengthUnit.CENTIMETER, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_FeetToYards(){
        assertEquals(2.0, QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS), EPSILON);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue(){
        double temp = 5.5;

        double inches = QuantityLength.convert(temp, LengthUnit.FEET, LengthUnit.INCH);
        double back = QuantityLength.convert(inches, LengthUnit.INCH, LengthUnit.FEET);
        assertEquals(temp, back, EPSILON);
    }

    @Test
    void testConversion_ZeroValue(){
        assertEquals(0.0, QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_NegativeValue(){
        assertEquals(-12.0, QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InvalidUnit_Throws(){
       assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(1.0, null, LengthUnit.FEET));
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH));
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH));
    }
}