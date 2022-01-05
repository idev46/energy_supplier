package sample.models;

public class Usage {
    private String month;
    private double units;
    private double amount;
    private String PaymentStatus;

    public Usage(String month, double units, double amount, String paymentStatus) {
        this.month = month;
        this.units = units;
        this.amount = amount;
        PaymentStatus = paymentStatus;
    }

    public Usage() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
