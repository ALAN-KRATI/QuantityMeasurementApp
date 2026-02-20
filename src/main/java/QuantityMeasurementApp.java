public class QuantityMeasurementApp{
    public static class Feet{
        private final double value;

        public Feet(double value){
            this.value = value;
        }

        public double getValue(){ return value;}

        @Override
        public boolean equals(Object obj){
            if(this == obj) return true;
            if(obj == null) return false;

            if(getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode(){
            return Double.hashCode(value);
        }
    }

    public static void main(String[] args){
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);

        if(f1.equals(f2)) System.out.println("Both the Feet values are equal!");
        else System.out.println("They are not equal!");
    }
}
