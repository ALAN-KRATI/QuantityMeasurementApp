public class QuantityMeasurementApp {
    public static void main(String[] args) {

        Length yard = new Length(1, Length.LengthUnit.YARDS);
        Length feet = new Length(3, Length.LengthUnit.FEET);
        Length inch = new Length(36, Length.LengthUnit.INCH);
        Length cm = new Length(1, Length.LengthUnit.CENTIMETER);

        System.out.println(yard.equals(feet));
        System.out.println(yard.equals(inch));
        System.out.println(cm.equals(new Length(0.393701, Length.LengthUnit.INCH)));
    }
}