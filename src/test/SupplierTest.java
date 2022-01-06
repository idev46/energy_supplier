package test;

import junit.framework.TestCase;
import sample.models.Consumer;
import sample.models.Reading;
import sample.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class SupplierTest extends TestCase {
    protected ArrayList<Reading> readings;
    protected Consumer consumer;

    protected void setUp(){
        try {
            consumer= Utils.getConsumerByAccountNumber("CONSUMER-1640978348");
            readings = Utils.getAllReadingsByAccountNo("CONSUMER-1640978348");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testVerifyConsumerType(){
        assertEquals("Non-Commercial", consumer.getEnergyTariff());
    }

    public void testVerifyConsumerAccount(){
        assertEquals("CONSUMER-1640978347", consumer.getAccountNumber());
    }
    public void testVerifyTotalBill(){
        assertEquals(25155.0,readings.get(readings.size()-1).getTotalCost());
    }

    public void testVerifyTaxesOnBill(){
        assertEquals(5655.0,readings.get(readings.size()-1).getTaxes());
    }

    public void testVerifyCostWithoutTaxes(){
        assertEquals(19500.0,readings.get(readings.size()-1).getCostWithoutTaxes());
    }

    public void testVerifyCostPerUnit(){
        assertEquals(50,Integer.parseInt(readings.get(readings.size()-1).getCostPerUnit()));
    }

    public void testVerifyCurrentUnits(){
        assertEquals(390.0,readings.get(readings.size()-1).getCurrentUsedUnits());
    }
}