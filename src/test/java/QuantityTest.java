import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
    @Test
    void testEquality_YardToYard_SameValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(1.0, Length.LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToYard_DifferentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(2.0, Length.LengthUnit.YARDS);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(3.0, Length.LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue(){
        Length q1 = new Length(3.0, Length.LengthUnit.FEET);
        Length q2 = new Length(1.0, Length.LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(36.0, Length.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue(){
        Length q1 = new Length(36.0, Length.LengthUnit.INCH);
        Length q2 = new Length(1.0, Length.LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(2.0, Length.LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_centimetersToInches_EquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.CENTIMETER);
        Length q2 = new Length(0.393701, Length.LengthUnit.INCH);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_centimetersToFeet_NonEquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.CENTIMETER);
        Length q2 = new Length(1.0, Length.LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        Length q2 = new Length(3.0, Length.LengthUnit.FEET);
        Length q3 = new Length(36.0, Length.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
        assertTrue(q2.equals(q3));
        assertTrue(q1.equals(q3));
    }

    @Test
    void testEquality_YardWithNullUnit(){
        assertThrows(IllegalArgumentException.class, () -> new Length(1, null));
    }

    @Test
    void testEquality_YardSameReference(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_YardNullComparison(){
        Length q1 = new Length(1.0, Length.LengthUnit.YARDS);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Length(2, null));
    }

    @Test 
    void testEquality_CentimetersSameReference(){
        Length q1 = new Length(1.0, Length.LengthUnit.CENTIMETER);
        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_centimeterNullComparison(){
        Length q1 = new Length(1.0, Length.LengthUnit.CENTIMETER);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario(){
        assertTrue(new Length(2, Length.LengthUnit.YARDS).equals(new Length(6, Length.LengthUnit.FEET)));
        assertTrue(new Length(6, Length.LengthUnit.FEET).equals(new Length(72, Length.LengthUnit.INCH)));
    }
}