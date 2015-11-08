package com.go.euro.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.euro.test.exception.DataReaderException;
import com.go.euro.test.domain.City;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.security.action.GetPropertyAction;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.AccessController;

public class DataReader {

    private static final String CITY_API = "http://api.goeuro.com/api/v2/position/suggest/en/";

    public City[] getCityData(String cityName) {
        return getCityData(CITY_API, cityName);
    }

    City[] getCityData(String api, String cityName) {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            final HttpGet httpGet = new HttpGet(api + URLEncoder.encode(cityName, "UTF-8"));
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new DataReaderException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            entity = response.getEntity();
            final InputStream inputStream = entity.getContent();
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, City[].class);
        } catch (final UnsupportedEncodingException e) {
            throw new DataReaderException("Url encoding not supported : ", e);
        } catch (final IOException e) {
            if (causeIsTimeoutException(e)) {
                throw new DataReaderException("Time out exception : ", e);
            } else if (causeIsJsonException(e)) {
                throw new DataReaderException("Json processing exception : ", e);
            } else {
                throw new DataReaderException("Reader exception : ", e);
            }
        } finally {
            try {
                EntityUtils.consume(entity);
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                throw new DataReaderException("Reader exception: ", e);
            }
        }
    }

    private boolean causeIsTimeoutException(final Exception e) {
        return e.getCause() instanceof ConnectTimeoutException || e.getCause() instanceof SocketTimeoutException;
    }

    private boolean causeIsJsonException(final Exception e) {
        return e.getCause() instanceof JsonParseException || e.getCause() instanceof JsonMappingException;
    }
}
