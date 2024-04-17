package org.example.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return rating.getValue();
    }

    @Override
    public Rating convertToEntityAttribute(String s) {
        Rating[] values = Rating.values();

        for (Rating value: values){
            if (value.getValue().equals(s))
                return value;
        }
        return null;
    }
}
