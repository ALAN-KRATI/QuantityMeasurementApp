public class QuantityMeasurementApp {

    public static void main(String[] args) {
        Quantity f1 = new Quantity(1.0, Unit.FEET);
        Quantity f2 = new Quantity(1.0, Unit.FEET);

        System.out.println(f1.equals(f2));

        Quantity f = new Quantity(1.0, Unit.FEET);
        Quantity inch = new Quantity(12.0, Unit.INCH);

        System.out.println(f.equals(inch));

        Quantity inch1 = new Quantity(11.0, Unit.INCH);

        System.out.println(f.equals(inch1));
    }
}