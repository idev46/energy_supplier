package sample.models;

import sample.utils.Utils;

public class Reading {
    private Consumer consumer;
    private String openingReadings;
    private String closingReadings;
    private String month;
    private String year;
    //private boolean paymentStatus;
    private String paymentStatus;
    private String costPerUnit;
    private int recordNo;
    //private String readingDate;

    //Get current used units from open and close readings
    public double getCurrentUsedUnits() {
        double temp = Double.parseDouble(closingReadings) - Double.parseDouble(openingReadings);
        return Math.max(temp, 0);
    }

    //get taxes for consumed energy cost
    public double getTaxes() {
        double cost = getCostWithoutTaxes();
        double vat = cost * Utils.VAT;
        double gst = cost * Utils.GST;
        double additionalCharges = cost * Utils.ADDITIONAL_CHARGES;
        return vat + gst + additionalCharges;
    }

    //get total cost with taxes
    public double getTotalCost() {
        return getTaxes() + getCostWithoutTaxes();
    }

    //get total cost without taxes
    public double getCostWithoutTaxes() {
        return getCurrentUsedUnits() * Double.parseDouble(costPerUnit);
    }


    public Consumer getConsumer() {
        return consumer;
    }

    //Getters and Setters
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getOpeningReadings() {
        return openingReadings;
    }

    public void setOpeningReadings(String openingReadings) {
        this.openingReadings = openingReadings;
    }

    public String getClosingReadings() {
        return closingReadings;
    }

    public void setClosingReadings(String closingReadings) {
        this.closingReadings = closingReadings;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(String costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public int getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
    }
}
