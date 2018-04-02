package com.softuni.musichub.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public <S, D> List<D> convertAll(Iterable<S> source, Class<D> destination) {
        List<D> convertedItems = new ArrayList<>();
        for (S s : source) {
            D convertedItem = this.getModelMapper().map(s, destination);
            convertedItems.add(convertedItem);
        }

        return convertedItems;
    }

    public <S, D> Page<D> convertToPage(Pageable pageable,
            Page<S> source, Class<D> destination) {
        List<S> pageContent = source.getContent();
        List<D> pageMappedContent = this.convertAll(pageContent, destination);
        Long totalElements = source.getTotalElements();
        return new PageImpl<>(pageMappedContent, pageable, totalElements);
    }
}
