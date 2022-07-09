package com.okay.jaspertest.service;

import com.okay.jaspertest.model.AuthorDto;
import com.okay.jaspertest.model.BookDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyServiceImpl implements DummyService {

    @Override
    public List<BookDto> createDummyBookList(int size) {
        List<BookDto> bookList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            bookList.add(BookDto.builder().name("name" + i).description("description" + i).build());
        }

        return bookList;
    }

    @Override
    public List<AuthorDto> createDummyAuthorList(int size) {
        List<AuthorDto> authorList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            authorList.add(AuthorDto.builder().name("author" + i).description("author" + i).build());
        }

        return authorList;
    }

    @Override
    public List<List<Object>> createDummyList(int size) {
        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Object> rowList = new ArrayList<>();
            rowList.add("name" + i);
            rowList.add("description" + i);
            dataList.add(rowList);
        }

        return dataList;
    }
}