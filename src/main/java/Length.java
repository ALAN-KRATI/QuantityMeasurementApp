import java.util.Objects;

public class Length {
    private final double value;
    private final LengthUnit unit;

    public enum LengthUnit {
        FEET(1.0),
        INCH(1.0/12),
        YARDS(3.0),
        CENTIMETER(0.03280839895);

        private final double convertToFeet;

        LengthUnit(double convertToFeet){
            this.convertToFeet = convertToFeet;
        }

        public double toFeet(double value){
            return value * convertToFeet;
        }

        public double fromFeet(double value){
            return value/convertToFeet;
        }

        public double getFeetFactor(){ return convertToFeet; }

    }

    public Length(double value, LengthUnit unit){
        this.value = value;
        this.unit = unit;
    }

    private double toBaseValue(){
        return unit.toFeet(value);
    }

    public Length add(Length q){
        if(q == null){
            throw new IllegalArgumentException("Cannot add null quantity!");
        }

        double first = this.toBaseValue();
        double second = q.toBaseValue();
        double sum = first + second;
        double ans = this.unit.fromFeet(sum);

        return new Length(ans, this.unit);
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

    @Override
    public String toString(){
        return value + " " + unit;
    }
}
