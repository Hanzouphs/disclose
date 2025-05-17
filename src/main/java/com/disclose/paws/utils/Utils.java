package com.disclose.paws.utils;

import com.disclose.paws.model.dto.AddressDTO;
import com.disclose.paws.model.dto.ContactDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static String getValueOrDefault(Object value) {
        if (value == null) {
            return "Not informed";
        }
        if (value instanceof String && ((String) value).isEmpty()) {
            return "Not informed";
        }
        return value.toString();
    }

    public static AddressDTO createAddressFrom(String street, String city,
                                               String state, String country,
                                               String postalCode) {
        return AddressDTO.builder()
                .street(street)
                .city(city)
                .state(state)
                .country(country)
                .postalCode(postalCode)
                .build();
    }

    public static ContactDTO createContactFrom(String email, String phoneNumber) {
        return ContactDTO.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

}
