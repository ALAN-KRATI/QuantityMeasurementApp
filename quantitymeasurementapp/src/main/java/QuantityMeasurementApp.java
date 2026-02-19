public class QuantityMeasurementApp {

    public static void main(String[] args) {
        QuantityLength f1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength f2 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength inch1 = new QuantityLength(24.0, LengthUnit.INCH);

        System.out.println(f1.equals(inch));
        System.out.println(f2.equals(inch1));
        System.out.println(f1.equals(f2));
    }
}