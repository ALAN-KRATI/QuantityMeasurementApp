public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Length oneFoot = new Length(1.0, Length.LengthUnit.FEET);
        Length twelveInch = new Length(12.0, Length.LengthUnit.INCH);
        Length oneYard = new Length(1.0, Length.LengthUnit.YARDS);
        Length cm = new Length(30.48, Length.LengthUnit.CENTIMETER); // ≈ 1 foot

        System.out.println("1 ft == 12 in : " + oneFoot.equals(twelveInch));
        System.out.println("1 yard == 3 ft : " + oneYard.equals(new Length(3.0, Length.LengthUnit.FEET)));
        System.out.println("30.48 cm == 1 ft : " + cm.equals(oneFoot));

        Length feet = new Length(1.0, Length.LengthUnit.FEET);
        Length inch = new Length(2.0, Length.LengthUnit.INCH);

        Length result1 = feet.add(inch);
        System.out.println("1 ft + 2 in = " + result1);

     
        Length result2 = inch.add(feet);
        System.out.println("2 in + 1 ft = " + result2);


        Length yard = new Length(1.0, Length.LengthUnit.YARDS);
        Length feetTwo = new Length(2.0, Length.LengthUnit.FEET);

        Length result3 = yard.add(feetTwo);
        System.out.println("1 yard + 2 ft = " + result3);
        Length zero = new Length(0.0, Length.LengthUnit.FEET);
        Length negative = new Length(-2.0, Length.LengthUnit.FEET);

        System.out.println("1 ft + 0 ft = " + feet.add(zero));
        System.out.println("5 ft + (-2 ft) = " + new Length(5.0, Length.LengthUnit.FEET).add(negative));
    }
}
