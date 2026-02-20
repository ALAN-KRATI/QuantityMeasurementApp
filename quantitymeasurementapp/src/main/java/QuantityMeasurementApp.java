public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println("UC6 result (default LengthUnit): " + feet.add(inch));

        System.out.println("Target FEET: " + QuantityLength.add(feet, inch, LengthUnit.FEET));
        System.out.println("Target INCH: " + QuantityLength.add(feet, inch, LengthUnit.INCH));
        System.out.println("Target YARDS: " + QuantityLength.add(feet, inch, LengthUnit.YARDS));
        System.out.println("Target CM: " + QuantityLength.add(feet, inch, LengthUnit.CENTIMETER));

    }
}
