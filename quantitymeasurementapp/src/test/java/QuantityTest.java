import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
   @Test
   void testAddition_SameUnit_FeetPlusFeet(){
        QuantityLength ans = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength res = q1.add(new QuantityLength(2.0, LengthUnit.FEET));

        assertEquals(ans, res);
   }

   @Test
   void testAddition_SameUnit_InchPlusInch(){
        QuantityLength ans = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength q1 = new QuantityLength(6.0, LengthUnit.INCH);
        QuantityLength res = q1.add(new QuantityLength(6.0, LengthUnit.INCH));

        assertEquals(ans, res);
   }

    @Test
   void testAddition_SameUnit_InchPlusFeet(){
        QuantityLength ans = new QuantityLength(24.0, LengthUnit.INCH);
        QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength res = q1.add(new QuantityLength(1.0, LengthUnit.FEET));

        assertEquals(ans, res);
   }

   @Test
   void testAddition_SameUnit_FeetPlusInch(){
        QuantityLength ans = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength res = q1.add(new QuantityLength(12.0, LengthUnit.INCH));

        assertEquals(ans, res);
   }
}