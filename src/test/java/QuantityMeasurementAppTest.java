import controller.QuantityMeasurementController;
import model.QuantityMeasurementEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quantity.Quantity;
import service.IQuantityMeasurementService;
import service.QuantityMeasurementServiceImpl;
import units.LengthUnit;
import units.TemperatureUnit;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setup() {
        service = new QuantityMeasurementServiceImpl();
        controller = new QuantityMeasurementController(service);
    }

    // ---------------- ENTITY TESTS ----------------

    @Test
    void testQuantityEntity_SingleOperandConstruction() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        "CONVERT",
                        "1 FEET",
                        "12 INCH");

        assertEquals("CONVERT", entity.getOperation());
        assertEquals("1 FEET", entity.getOperand1());
        assertEquals("12 INCH", entity.getResult());
        assertFalse(entity.hasError());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        "ADD",
                        "1 FEET",
                        "12 INCH",
                        "2 FEET");

        assertEquals("ADD", entity.getOperation());
        assertEquals("1 FEET", entity.getOperand1());
        assertEquals("12 INCH", entity.getOperand2());
        assertEquals("2 FEET", entity.getResult());
    }

    @Test
    void testQuantityEntity_ErrorConstruction() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("Invalid operation");

        assertTrue(entity.hasError());
        assertEquals("Invalid operation", entity.getError());
    }

    @Test
    void testQuantityEntity_ToString_Success() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        "ADD",
                        "1 FEET",
                        "12 INCH",
                        "2 FEET");

        assertTrue(entity.toString().contains("ADD"));
    }

    @Test
    void testQuantityEntity_ToString_Error() {

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("Failure");

        assertTrue(entity.toString().contains("ERROR"));
    }

    // ---------------- SERVICE TESTS ----------------

    @Test
    void testService_CompareEquality_SameUnit_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(1, LengthUnit.FEET);

        QuantityMeasurementEntity entity =
                service.compare(q1, q2);

        assertFalse(entity.hasError());
        assertTrue(entity.getResult().contains("true"));
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCH);

        QuantityMeasurementEntity entity =
                service.compare(q1, q2);

        assertFalse(entity.hasError());
    }

    @Test
    void testService_Convert_Success() {

        Quantity<LengthUnit> q =
                new Quantity<>(1, LengthUnit.FEET);

        QuantityMeasurementEntity entity =
                service.convert(q, LengthUnit.INCH);

        assertFalse(entity.hasError());
        assertTrue(entity.getResult().contains("INCH"));
    }

    @Test
    void testService_Add_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCH);

        QuantityMeasurementEntity entity =
                service.add(q1, q2);

        assertFalse(entity.hasError());
    }

    @Test
    void testService_Subtract_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(2, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCH);

        QuantityMeasurementEntity entity =
                service.subtract(q1, q2);

        assertFalse(entity.hasError());
    }

    @Test
    void testService_Divide_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(2, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(1, LengthUnit.FEET);

        QuantityMeasurementEntity entity =
                service.divide(q1, q2);

        assertFalse(entity.hasError());
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {

        Quantity<TemperatureUnit> t1 =
                new Quantity<>(0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> t2 =
                new Quantity<>(32, TemperatureUnit.FAHRENHEIT);

        QuantityMeasurementEntity entity =
                service.add(t1, t2);

        assertTrue(entity.hasError());
    }

    // ---------------- CONTROLLER TESTS ----------------

    @Test
    void testController_DemonstrateEquality_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCH);

        assertDoesNotThrow(() ->
                controller.demonstrateEquality(q1, q2));
    }

    @Test
    void testController_DemonstrateAddition_Success() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCH);

        assertDoesNotThrow(() ->
                controller.demonstrateAddition(q1, q2));
    }

    @Test
    void testController_NullService_Prevention() {

        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementController(null));
    }

}