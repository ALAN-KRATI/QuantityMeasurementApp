public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Length f1 = new Length(1.0, Length.LengthUnit.FEET);
        Length inch = new Length(12.0, Length.LengthUnit.INCH);

        Length f2 = new Length(2.0, Length.LengthUnit.FEET);
        Length inch1 = new Length(24.0, Length.LengthUnit.INCH);

        System.out.println(f1.equals(inch));
        System.out.println(f2.equals(inch1));
        System.out.println(f1.equals(f2));
    }
}