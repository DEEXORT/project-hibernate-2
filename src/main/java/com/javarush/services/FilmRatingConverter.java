package com.javarush;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FilmRatingConverter implements AttributeConverter<Film.Rating, String> {

    @Override
    public String convertToDatabaseColumn(Film.Rating attribute) {
        return attribute.getDbValue();
    }

    @Override
    public Film.Rating convertToEntityAttribute(String dbData) {
        return Film.Rating.fromDbValue(dbData);
    }
}
