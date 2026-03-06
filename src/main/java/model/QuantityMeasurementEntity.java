package model;

public final class QuantityMeasurementEntity {

    private final String operation;
    private final String operand1;
    private final String operand2;
    private final String result;
    private final String error;

    // Conversion constructor (single operand)
    public QuantityMeasurementEntity(String operation,
                                     String operand1,
                                     String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = null;
        this.result = result;
        this.error = null;
    }

    // Binary operation constructor
    public QuantityMeasurementEntity(String operation,
                                     String operand1,
                                     String operand2,
                                     String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
        this.error = null;
    }

    // Error constructor
    public QuantityMeasurementEntity(String errorMessage) {
        this.operation = null;
        this.operand1 = null;
        this.operand2 = null;
        this.result = null;
        this.error = errorMessage;
    }

    public boolean hasError() {
        return error != null;
    }

    public String getOperation() {
        return operation;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {

        if (hasError()) {
            return "ERROR: " + error;
        }

        if (operand2 == null) {
            return operation + " (" + operand1 + ") = " + result;
        }

        return operation + " (" + operand1 + ", " + operand2 + ") = " + result;
    }
}