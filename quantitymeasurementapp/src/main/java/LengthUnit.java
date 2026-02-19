public enum LengthUnit {
    FEET(1.0),
    INCH(1.0/12),
    YARDS(3.0),
    CENTIMETER(0.393701);

    private final double convertToFeet;

    LengthUnit(double convertToFeet){
        this.convertToFeet = convertToFeet;
    }

    public double convertToBase(double value){
        return value * convertToFeet;
    }

}
