import java.util.Objects;

public class Quantity {
    private final double value;
    private final Unit unit;

    public Quantity(double value, Unit unit){
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

        Quantity other = (Quantity) obj;
        return Double.compare(this.toBaseValue(), other.toBaseValue()) == 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(toBaseValue());
    }
}
