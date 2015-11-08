package com.go.euro.test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    @JsonProperty("_id")
    private Long id;

    private Object key;

    private String name;

    private String fullName;

    @JsonProperty("iata_airport_code")
    private String iataAirportCode;

    private String type;

    private String country;

    private Long locationId;

    private Boolean inEurope;

    private String countryCode;

    private Boolean coreCountry;

    private Object distance;

    @JsonProperty("geo_position")
    private GeoPosition geoPosition;
}