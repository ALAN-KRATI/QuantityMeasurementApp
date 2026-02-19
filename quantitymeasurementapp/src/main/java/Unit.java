public enum Unit {
    FEET(12.0),
    INCH(1.0);

    private final double convertToInch;

    Unit(double convertToInch){
        this.convertToInch = convertToInch;
    }

    public double convertToBase(double value){
        return value * convertToInch;
    }

}
