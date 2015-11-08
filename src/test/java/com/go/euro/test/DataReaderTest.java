package com.go.euro.test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.go.euro.test.exception.DataReaderException;
import com.go.euro.test.domain.City;
import org.junit.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class DataReaderTest {

    private DataReader dataReader;

    @Before
    public void init() {
        dataReader = new DataReader();
    }

    @Rule
    public WireMockRule mockRule = new WireMockRule(TestConstants.PORT);

    @Test
    public void testSuccessfulResponse(){
        stubFor(get(urlEqualTo(TestConstants.CITY_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("reader_test.json")));
        City[] cityInfos = dataReader.getCityData(TestConstants.LOCALHOST, TestConstants.CITY);
        assertEquals(8, cityInfos.length);
    }

    @Test(expected = DataReaderException.class)
    public void testServerResponseException() {
        stubFor(get(urlEqualTo(TestConstants.CITY_PATH))
                .willReturn(aResponse()
                        .withStatus(404)));
        dataReader.getCityData(TestConstants.LOCALHOST, TestConstants.CITY);
    }

    @Test(expected = DataReaderException.class)
    public void testJsonProcessingException()  {
        stubFor(get(urlEqualTo(TestConstants.CITY_PATH))
                .willReturn(aResponse()
                        .withStatus(200)));
        dataReader.getCityData(TestConstants.LOCALHOST, TestConstants.CITY);
    }
}