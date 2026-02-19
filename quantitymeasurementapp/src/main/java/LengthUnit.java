public enum LengthUnit {
    FEET(1.0),
    INCH(1.0/12.0);

    private final double convertToFeet;

    LengthUnit(double convertToInch){
        this.convertToFeet = convertToInch;
    }

    public double convertToBase(double value){
        return value * convertToFeet;
    }

}
