import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuantityWeightTest {
    @Test
    void testEquality_KilogramToKilogram_SameValue(){
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertTrue(w1.equals(w2));
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue(){
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(2.0, WeightUnit.KILOGRAM);

        assertFalse(w1.equals(w2));
    }

     @Test
    void testEquality_GramToKilogram_EquivalentValue() {
        assertEquals(new QuantityWeight(1000.0, WeightUnit.GRAM),
                     new QuantityWeight(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_WeightVsLength_Incompatible() {
        QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityLength length = new QuantityLength(1.0, LengthUnit.FEET);

        assertFalse(weight.equals(length));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(null));
    }

    @Test
    void testEquality_SameReference() {
        QuantityWeight w = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(w.equals(w));
    }

    @Test
    void testConversion_PoundToKilogram() {
        QuantityWeight result =
                new QuantityWeight(2.20462, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), result);
    }

    @Test
    void testConversion_KilogramToPound() {
        QuantityWeight result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.POUND);

        assertEquals(new QuantityWeight(2.20462, WeightUnit.POUND), result);
    }

    @Test
    void testConversion_SameUnit() {
        QuantityWeight result =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(new QuantityWeight(5.0, WeightUnit.KILOGRAM), result);
    }

     @Test
    void testConversion_ZeroValue() {
        assertEquals(new QuantityWeight(0.0, WeightUnit.GRAM),
                new QuantityWeight(0.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM));
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(new QuantityWeight(-1000.0, WeightUnit.GRAM),
                new QuantityWeight(-1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM));
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityWeight original = new QuantityWeight(1.5, WeightUnit.KILOGRAM);
        QuantityWeight result =
                original.convertTo(WeightUnit.GRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(original, result);
    }

     
    @Test
    void testAddition_WithZero() {
        assertEquals(new QuantityWeight(5.0, WeightUnit.KILOGRAM),
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(0.0, WeightUnit.GRAM)));
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(new QuantityWeight(3.0, WeightUnit.KILOGRAM),
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(-2000.0, WeightUnit.GRAM)));
    }
}
