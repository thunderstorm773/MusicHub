package com.softuni.musichub.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MapperUtil {

    private ModelMapper modelMapper;

    public MapperUtil() {
        this.modelMapper = new ModelMapper();
    }

    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    public <S, D> List<D> convertAll(List<S> source, Class<D> destination) {
        List<D> convertedItems = new ArrayList<>();
        for (S s : source) {
            D convertedItem = this.getModelMapper().map(s, destination);
            convertedItems.add(convertedItem);
        }

        return convertedItems;
    }
}
