package sample.models;

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

    public int getCurrentUsedUnits(){
        int temp = Integer.parseInt(closingReadings)-Integer.parseInt(openingReadings);
        return temp = Math.max(temp, 0);
    }
    public Consumer getConsumer() {
        return consumer;
    }

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
