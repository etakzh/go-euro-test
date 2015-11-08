package com.go.euro.test;

import com.go.euro.test.domain.City;

import java.util.Scanner;

public class FileCreator {

    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Please, specify city name:");
            if (in.hasNextLine()) {
                final String cityName = in.nextLine();
                if (cityName.length() == EXIT_COMMAND.length() && cityName.toLowerCase().equals(EXIT_COMMAND)) {
                    System.out.println("Exiting...");
                    in.close();
                    return;
                }
                final DataReader dataReader = new DataReader();
                final City[] cities = dataReader.getCityData(cityName);
                DataWriter.writeToFile(cityName, cities);
                System.out.println("File " + cityName.replaceAll(" ", "_") + ".csv is created");
            }
        }
    }
}
