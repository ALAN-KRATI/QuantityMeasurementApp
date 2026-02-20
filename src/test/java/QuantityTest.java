import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {
   @Test
   void testAddition_SameUnit_FeetPlusFeet(){
        Length ans = new Length(3.0, Length.LengthUnit.FEET);
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        Length res = q1.add(new Length(2.0, Length.LengthUnit.FEET));

        assertEquals(ans, res);
   }

   @Test
   void testAddition_SameUnit_InchPlusInch(){
        Length ans = new Length(12.0, Length.LengthUnit.INCH);
        Length q1 = new Length(6.0, Length.LengthUnit.INCH);
        Length res = q1.add(new Length(6.0, Length.LengthUnit.INCH));

        assertEquals(ans, res);
   }

    @Test
   void testAddition_SameUnit_InchPlusFeet(){
        Length ans = new Length(24.0, Length.LengthUnit.INCH);
        Length q1 = new Length(12.0, Length.LengthUnit.INCH);
        Length res = q1.add(new Length(1.0, Length.LengthUnit.FEET));

        assertEquals(ans, res);
   }

   @Test
   void testAddition_SameUnit_FeetPlusInch(){
        Length ans = new Length(2.0, Length.LengthUnit.FEET);
        Length q1 = new Length(1.0, Length.LengthUnit.FEET);
        Length res = q1.add(new Length(12.0, Length.LengthUnit.INCH));

        assertEquals(ans, res);
   }
}