import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    @Test
    void yardToFeet_shouldBeEqual() {
        Quantity yard = new Quantity(1.0, Unit.YARDS);
        Quantity feet = new Quantity(3.0, Unit.FEET);

        assertEquals(yard, feet);
    }

    @Test
    void yardToInch_shouldBeEqual() {
        Quantity yard = new Quantity(1.0, Unit.YARDS);
        Quantity inch = new Quantity(36.0, Unit.INCH);

        assertEquals(yard, inch);
    }

    @Test
    void centimeterToInch_shouldBeEqual() {
        Quantity cm = new Quantity(1.0, Unit.CENTIMETER);
        Quantity inch = new Quantity(0.393701, Unit.INCH);

        assertEquals(cm, inch);
    }

    @Test
    void centimeterToFeet_shouldNotBeEqual() {
        Quantity cm = new Quantity(1.0, Unit.CENTIMETER);
        Quantity feet = new Quantity(1.0, Unit.FEET);

        assertNotEquals(cm, feet);
    }

    @Test
    void multiUnitTransitiveProperty() {
        Quantity yard = new Quantity(1.0, Unit.YARDS);
        Quantity feet = new Quantity(3.0, Unit.FEET);
        Quantity inch = new Quantity(36.0, Unit.INCH);

        assertEquals(yard, feet);
        assertEquals(feet, inch);
        assertEquals(yard, inch);
    }
}