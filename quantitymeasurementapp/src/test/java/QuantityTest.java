import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    @Test
    void feetEquality_SameValue() {
        Quantity q1 = new Quantity(1, Unit.FEET);
        Quantity q2 = new Quantity(1, Unit.FEET);

        assertEquals(q1, q2);
    }

    @Test
    void feetEquality_DifferentValue() {
        Quantity q1 = new Quantity(1, Unit.FEET);
        Quantity q2 = new Quantity(2, Unit.FEET);

        assertNotEquals(q1, q2);
    }

    @Test
    void feetAndInchEquality_SameValueDifferentUnit() {
        Quantity feet = new Quantity(1, Unit.FEET);
        Quantity inch = new Quantity(12, Unit.INCH);

        assertEquals(feet, inch);
    }

    @Test
    void feetAndInchEquality_DifferentValue() {
        Quantity feet = new Quantity(1, Unit.FEET);
        Quantity inch = new Quantity(11, Unit.INCH);

        assertNotEquals(feet, inch);
    }

    @Test
    void QuantityEquality_Null() {
        Quantity q = new Quantity(1, Unit.FEET);
        assertNotEquals(q, null);
    }
}