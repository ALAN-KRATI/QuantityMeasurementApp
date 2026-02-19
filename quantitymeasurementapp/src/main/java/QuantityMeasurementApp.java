public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength yard = new QuantityLength(1, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(36, LengthUnit.INCH);
        QuantityLength cm = new QuantityLength(1, LengthUnit.CENTIMETER);

        System.out.println(yard.equals(feet));
        System.out.println(yard.equals(inch));
        System.out.println(cm.equals(new QuantityLength(0.393701, LengthUnit.INCH)));
    }
}