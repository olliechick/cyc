package seng202.team1;

import org.junit.Test;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GenerateFieldsTest {

    private String csv_resource_dir = "src/test/resources/";

    @Test
    public void TestPrimaryFunctions(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);
        assertEquals(3,primaryFunctions.size());
    }
    @Test
    public void TestPrimaryFunctionsLargeList(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "Lower_Manhattan_Retailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);

        assertEquals(8,primaryFunctions.size());
    }

    @Test
    public void TestSecondaryFunctions(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        assertEquals(4, secondaryFunctions.size());
    }

    @Test
    public void TestSecondaryFunctionsLargeList(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir +"Lower_Manhattan_Retailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        assertEquals(92, secondaryFunctions.size());
    }

    @Test
    public void TestSameTypeListPrimary(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);
        ArrayList<RetailerLocation> results = GenerateFields.generateListOfSameFunction(retailers, primaryFunctions.get(0),true );
        assertEquals(8, results.size());
    }
    @Test
    public void TestSameTypeListSecondary(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        ArrayList<RetailerLocation> results = GenerateFields.generateListOfSameFunction(retailers, secondaryFunctions.get(0),false );
        assertEquals(8, results.size());
    }

    @Test
    public void TestGenerateWifiPointTypesSmallList(){
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir +"wifiTester.csv");
        ArrayList<String> results = GenerateFields.generateWifiTypes(hotspots);
        assertEquals(1,results.size());
    }
    @Test
    public void TestGenerateWifiPointTypesLargeList(){
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir +"NYC_Free_Public_WiFi_03292017.csv");
        ArrayList<String> results = GenerateFields.generateWifiTypes(hotspots);
        assertEquals(3,results.size());
    }
    @Test
    public void TestFindWifiSpotsOfSameCost(){
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "wifiTester.csv");
        ArrayList<WifiPoint> results = GenerateFields.findPointsOfSameCost("free", hotspots);
        assertEquals(hotspots.size(),results.size());
    }
    @Test
    public void TestGenerateProvidersSmallList(){
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "wifiTester.csv");
        ArrayList<String> providers = GenerateFields.generateWifiProviders(hotspots);
        assertEquals(1, providers.size());
    }

    @Test
    public void TestGenerateProvidersLargeList(){
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "NYC_Free_Public_WiFi_03292017.csv");
        ArrayList<String> providers = GenerateFields.generateWifiProviders(hotspots);
        assertEquals(15, providers.size());
    }


}