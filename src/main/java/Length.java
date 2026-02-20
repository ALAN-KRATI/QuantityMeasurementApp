import java.util.Objects;

public class Length {
    private final double value;
    private final LengthUnit unit;

    public static enum LengthUnit {
        FEET(12.0),
        INCH(1.0);

        private final double conversionFactor;

        LengthUnit(double conversionFactor){
            this.conversionFactor = conversionFactor;
        }

        public double convertToBase(double value){
            return value * conversionFactor;
        }

        public double getConversionFactor(){ return conversionFactor; }

    }

    public Length(double value, LengthUnit unit){
        if(unit == null) throw new IllegalArgumentException("Unit cannot be null!");
        this.value = value;
        this.unit = unit;
    }

    private double toBaseValue(){
        return unit.convertToBase(value);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Length other = (Length) obj;
        return Double.compare(this.toBaseValue(), other.toBaseValue()) == 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(toBaseValue());
    }

    public double getValue(){ return value; }
    public LengthUnit getUnit(){ return unit; }
}
