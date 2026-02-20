import java.util.Objects;

public class Length {
    private final double value;
    private final LengthUnit unit;

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

    public Length(double value, LengthUnit unit){
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
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
}
