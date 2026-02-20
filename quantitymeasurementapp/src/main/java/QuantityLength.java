import java.util.Objects;

public class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit){
        this.value = value;
        this.unit = unit;
    }

    private double toBaseValue(){
        return unit.toFeet(value);
    }

    public QuantityLength add(QuantityLength q){
        if(q == null){
            throw new IllegalArgumentException("Cannot add null quantity!");
        }

        double first = this.toBaseValue();
        double second = q.toBaseValue();
        double sum = first + second;
        double ans = this.unit.fromFeet(sum);

        return new QuantityLength(ans, this.unit);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;
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
