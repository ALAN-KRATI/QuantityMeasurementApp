import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LengthTest {
    @Test
    void testEquality_FeetToFeet_SameValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        Length q2 = new Length(1.0, Length.LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }
    
    @Test
    void testEquality_InchToInch_SameValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.INCH);
        Length q2 = new Length(1.0, Length.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_NullComparison(){
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        assertFalse(q1.equals(null));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        Length q2 = new Length(12.0, Length.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        Length q2 = new Length(2.0, Length.LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_InchToInch_DifferentValue(){
        Length q1 = new Length(1.0, Length.LengthUnit.INCH);
        Length q2 = new Length(2.0, Length.LengthUnit.INCH);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_InvalidUnit(){
        assertThrows(IllegalArgumentException.class, () -> new Length(5.0, null));
    }

    @Test
    void testEquality_SameReference(){
        Length q1 = new Length(1, Length.LengthUnit.FEET);
        assertTrue(q1.equals(q1));
    }

}