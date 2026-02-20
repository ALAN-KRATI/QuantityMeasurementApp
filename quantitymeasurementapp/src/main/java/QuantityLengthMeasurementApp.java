public class QuantityLengthMeasurementApp {
    public static void main(String[] args) {
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength twelveInch = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength oneYard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength cm = new QuantityLength(30.48, LengthUnit.CENTIMETER); // ≈ 1 foot

        System.out.println("1 ft == 12 in : " + oneFoot.equals(twelveInch));
        System.out.println("1 yard == 3 ft : " + oneYard.equals(new QuantityLength(3.0, LengthUnit.FEET)));
        System.out.println("30.48 cm == 1 ft : " + cm.equals(oneFoot));

        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(2.0, LengthUnit.INCH);

        QuantityLength result1 = feet.add(inch);
        System.out.println("1 ft + 2 in = " + result1);

     
        QuantityLength result2 = inch.add(feet);
        System.out.println("2 in + 1 ft = " + result2);


        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feetTwo = new QuantityLength(2.0, LengthUnit.FEET);

        QuantityLength result3 = yard.add(feetTwo);
        System.out.println("1 yard + 2 ft = " + result3);
        QuantityLength zero = new QuantityLength(0.0, LengthUnit.FEET);
        QuantityLength negative = new QuantityLength(-2.0, LengthUnit.FEET);

        System.out.println("1 ft + 0 ft = " + feet.add(zero));
        System.out.println("5 ft + (-2 ft) = " + new QuantityLength(5.0, LengthUnit.FEET).add(negative));
    }
}
