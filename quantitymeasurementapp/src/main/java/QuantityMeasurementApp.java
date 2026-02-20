public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityWeight kg = new QuantityWeight(1, WeightUnit.KILOGRAM);
        QuantityWeight g = new QuantityWeight(1000, WeightUnit.GRAM);
        QuantityWeight lb = new QuantityWeight(2.20462, WeightUnit.POUND);

        System.out.println("1kg == 1000g : " + kg.equals(g));
        System.out.println("1kg == 2.20462lb : " + kg.equals(lb));

        System.out.println("1kg + 500g = " + kg.add(new QuantityWeight(500, WeightUnit.GRAM)));
        System.out.println("1kg + 1lb (in pounds) = " +
        QuantityWeight.add(kg, new QuantityWeight(1, WeightUnit.POUND), WeightUnit.POUND));

    }
}
