package seng202.team1;

import org.junit.Test;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.GenerateFields;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GenerateFieldsTest {

    private String csv_resource_dir = "src/test/resources/";

    @Test
    public void TestPrimaryFunctions() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);
        assertEquals(3,primaryFunctions.size());
    }
    @Test
    public void TestPrimaryFunctionsLargeList() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "Lower_Manhattan_Retailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);

        assertEquals(8,primaryFunctions.size());
    }

    @Test
    public void TestSecondaryFunctions() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        assertEquals(4, secondaryFunctions.size());
    }

    @Test
    public void TestSecondaryFunctionsLargeList() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir +"Lower_Manhattan_Retailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        assertEquals(92, secondaryFunctions.size());
    }

    @Test
    public void TestSameTypeListPrimary() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> primaryFunctions = GenerateFields.generatePrimaryFunctionsList(retailers);
        ArrayList<RetailerLocation> results = GenerateFields.generateListOfSameFunction(retailers, primaryFunctions.get(0),true );
        assertEquals(8, results.size());
    }
    @Test
    public void TestSameTypeListSecondary() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(csv_resource_dir + "testRetailers.csv");
        ArrayList<String> secondaryFunctions = GenerateFields.generateSecondaryFunctionsList(retailers);
        ArrayList<RetailerLocation> results = GenerateFields.generateListOfSameFunction(retailers, secondaryFunctions.get(0),false );
        assertEquals(8, results.size());
    }

    @Test
    public void TestGenerateWifiPointTypesSmallList() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir +"wifiTester.csv");
        ArrayList<String> results = GenerateFields.generateWifiTypes(hotspots);
        assertEquals(1,results.size());
    }
    @Test
    public void TestGenerateWifiPointTypesLargeList() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir +"NYC_Free_Public_WiFi_03292017.csv");
        ArrayList<String> results = GenerateFields.generateWifiTypes(hotspots);
        assertEquals(3,results.size());
    }
    @Test
    public void TestFindWifiSpotsOfSameCost() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "wifiTester.csv");
        ArrayList<WifiPoint> results = GenerateFields.findPointsOfSameCost("free", hotspots);
        assertEquals(hotspots.size(),results.size());
    }
    @Test
    public void TestGenerateProvidersSmallList() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "wifiTester.csv");
        ArrayList<String> providers = GenerateFields.generateWifiProviders(hotspots);
        assertEquals(1, providers.size());
    }

    @Test
    public void TestGenerateProvidersLargeList() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(csv_resource_dir + "NYC_Free_Public_WiFi_03292017.csv");
        ArrayList<String> providers = GenerateFields.generateWifiProviders(hotspots);
        assertEquals(15, providers.size());
    }


}