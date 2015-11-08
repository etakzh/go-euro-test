package com.go.euro.test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.go.euro.test.exception.DataReaderException;
import com.go.euro.test.domain.City;
import org.junit.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataWriterTest {

    private City[] cities;

    private DataReader dataReader;
    @Rule
    public WireMockRule mockRule = new WireMockRule(TestConstants.PORT);

    @Before
    public void createTestData() {
        dataReader = new DataReader();
        stubFor(get(urlEqualTo(TestConstants.CITY_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("reader_test.json")));
        this.cities = dataReader.getCityData(TestConstants.LOCALHOST, TestConstants.CITY);
    }

    @Test
    public void testSuccessfulWrite() throws IOException {
        DataWriter.writeToFile("test", cities);
        final File file = new File("test.csv");
        final List<String> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                rows.add(sCurrentLine);
            }
        }
        assertEquals(9, rows.size());
        assertTrue(rows.get(0).contains(DataWriter.CSV_FILE_HEADER[0]));
        assertTrue(rows.get(0).contains(DataWriter.CSV_FILE_HEADER[1]));
        assertTrue(rows.get(0).contains(DataWriter.CSV_FILE_HEADER[2]));
        assertTrue(rows.get(0).contains(DataWriter.CSV_FILE_HEADER[3]));
        assertTrue(rows.get(0).contains(DataWriter.CSV_FILE_HEADER[4]));
        for (int i = 0; i < cities.length; i++) {
            assertTrue(rows.get(i + 1).contains(cities[i].getId().toString()));
            assertTrue(rows.get(i + 1).contains(cities[i].getName()));
            assertTrue(rows.get(i + 1).contains(cities[i].getType()));
            assertTrue(rows.get(i + 1).contains(cities[i].getGeoPosition().getLatitude().toString()));
            assertTrue(rows.get(i + 1).contains(cities[i].getGeoPosition().getLongitude().toString()));
        }
    }

    @After
    public void deleteFile() {
        File file = new File("test.csv");
        if (file.exists()) {
            file.delete();
        }
    }

}
