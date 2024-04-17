package org.example.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Year;

@Converter(autoApply = true)
public class  YearConverter implements AttributeConverter<Year, Short> {
    @Override
    public Short convertToDatabaseColumn(Year year) {
        if (year == null)
            return null;

        return (short) year.getValue();
    }

    @Override
    public Year convertToEntityAttribute(Short year) {
        if (year == null)
            return null;

        return Year.of(year);
    }
}
