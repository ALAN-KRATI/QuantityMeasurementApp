package app;

import controller.QuantityMeasurementController;
import quantity.Quantity;
import repository.IQuantityMeasurementRepository;
import repository.QuantityMeasurementDatabaseRepository;
import service.QuantityMeasurementServiceImpl;
import units.LengthUnit;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        IQuantityMeasurementRepository repository = new QuantityMeasurementDatabaseRepository();

        QuantityMeasurementController controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl());

        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCH);

        controller.demonstrateEquality(l1, l2);
        controller.demonstrateAddition(l1, l2);
    }
}