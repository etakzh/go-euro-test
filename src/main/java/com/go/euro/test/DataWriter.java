package com.go.euro.test;

import com.go.euro.test.exception.DataWriterException;
import com.go.euro.test.domain.City;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataWriter {

    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int TYPE = 2;
    private static final int LATITUDE = 3;
    private static final int LONGITUDE = 4;
    private static final int COLUMN_NUM = 5;

    private static final String DEFAULT_FILE_NAME = "result.csv";
    private static final String EMPTY_STRING = "";
    static final String[] CSV_FILE_HEADER = {"_id", "name", "type", "latitude", "longitude"};

    public static void writeToFile(final String file, final City[] cities) {
        String fileName;
        if (file != null) {
            fileName = file.replaceAll(" ", "_") + ".csv";
        } else {
            fileName = DEFAULT_FILE_NAME;
        }
        final List<String[]> cityList = new ArrayList<>();
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(fileName), ',');
            writer.writeNext(CSV_FILE_HEADER);
            for (final City city : cities) {
                final String[] row = new String[COLUMN_NUM];
                if (city.getId() != null) {
                    row[ID] = city.getId().toString();
                } else {
                    row[ID] = EMPTY_STRING;
                }
                row[NAME] = city.getName();
                row[TYPE] = city.getType();
                if (city.getGeoPosition() != null && city.getGeoPosition().getLatitude() != null) {
                    row[LATITUDE] = city.getGeoPosition().getLatitude().toString();
                } else {
                    row[LATITUDE] = EMPTY_STRING;
                }
                if (city.getGeoPosition() != null && city.getGeoPosition().getLongitude() != null) {
                    row[LONGITUDE] = city.getGeoPosition().getLongitude().toString();
                } else {
                    row[LONGITUDE] = EMPTY_STRING;
                }
                cityList.add(row);
            }
            writer.writeAll(cityList);
        } catch (final IOException e) {
            throw new DataWriterException("Can't create csv file " + fileName + " : ", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (final IOException e) {
                throw new DataWriterException("Can't close the file " + fileName + " : ", e);
            }
        }
    }

}
