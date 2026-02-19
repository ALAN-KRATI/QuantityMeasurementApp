public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Quantity yard = new Quantity(1, Unit.YARDS);
        Quantity feet = new Quantity(3, Unit.FEET);
        Quantity inch = new Quantity(36, Unit.INCH);
        Quantity cm = new Quantity(1, Unit.CENTIMETER);

        System.out.println(yard.equals(feet));
        System.out.println(yard.equals(inch));
        System.out.println(cm.equals(new Quantity(0.393701, Unit.INCH)));
    }
}