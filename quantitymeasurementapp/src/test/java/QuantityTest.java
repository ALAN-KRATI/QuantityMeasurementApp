import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
    @Test
    void testEquality_YardToYard_SameValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToYard_DifferentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.YARDS);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue(){
        QuantityLength q1 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(36.0, LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue(){
        QuantityLength q1 = new QuantityLength(36.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_centimetersToInches_EquivalentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength q2 = new QuantityLength(0.393701, LengthUnit.INCH);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_centimetersToFeet_NonEquivalentValue(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCH);

        assertTrue(q1.equals(q2));
        assertTrue(q2.equals(q3));
        assertTrue(q1.equals(q3));
    }

    @Test
    void testEquality_YardWithNullUnit(){
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1, null));
    }

    @Test
    void testEquality_YardSameReference(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_YardNullComparison(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(2, null));
    }

    @Test 
    void testEquality_CentimetersSameReference(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_centimeterNullComparison(){
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario(){
        assertTrue(new QuantityLength(2, LengthUnit.YARDS).equals(new QuantityLength(6, LengthUnit.FEET)));

        assertTrue(new QuantityLength(6, LengthUnit.FEET).equals(new QuantityLength(72, LengthUnit.INCH)));
    }
}