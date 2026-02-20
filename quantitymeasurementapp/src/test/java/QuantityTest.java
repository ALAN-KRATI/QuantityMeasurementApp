import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
    @Test
    void testAddition_ExplicitTarget_Feet(){
        QuantityLength res = QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET);
        QuantityLength ans = new QuantityLength(2.0, LengthUnit.FEET);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_Inches(){
        QuantityLength res = QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH);
        QuantityLength ans = new QuantityLength(24.0, LengthUnit.INCH);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_Yards(){
        QuantityLength res = QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength ans = new QuantityLength(0.667, LengthUnit.YARDS);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_Centimeters(){
        QuantityLength res = QuantityLength.add(new QuantityLength(1.0, LengthUnit.INCH), new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETER);
        QuantityLength ans = new QuantityLength(5.08, LengthUnit.CENTIMETER);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_SameAsSecondOperand(){
        QuantityLength res = QuantityLength.add(new QuantityLength(2.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.FEET);
        QuantityLength ans = new QuantityLength(9.0, LengthUnit.FEET);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_Commutativity(){
        QuantityLength res1 = QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength res2 =  QuantityLength.add(new QuantityLength(12.0, LengthUnit.INCH), new QuantityLength(1.0, LengthUnit.FEET), LengthUnit.YARDS);
        assertEquals(res1, res2);
    }

    @Test
    void testAddition_ExplicitTarget_WithZero(){
        QuantityLength res = QuantityLength.add(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength ans = new QuantityLength(1.667, LengthUnit.YARDS);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_NegativeValues(){
        QuantityLength res = QuantityLength.add(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH);
        QuantityLength ans = new QuantityLength(36.0, LengthUnit.INCH);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_NullTargetUnit(){
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH), null));
    }

    @Test
    void testAddition_ExplicitTarget_LargeToSmallScale(){
        QuantityLength res = QuantityLength.add(new QuantityLength(1000.0, LengthUnit.FEET), new QuantityLength(500.0, LengthUnit.FEET), LengthUnit.INCH);
        QuantityLength ans = new QuantityLength(18000.0, LengthUnit.INCH);

        assertEquals(ans, res);
    }

    @Test
    void testAddition_ExplicitTarget_SamllToLargeScale(){
        QuantityLength res = QuantityLength.add(new QuantityLength(12.0, LengthUnit.INCH), new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);
        QuantityLength ans = new QuantityLength(0.667, LengthUnit.YARDS);

        assertEquals(ans, res);
    }

}