package com.okay.jaspertest.service;

import com.okay.jaspertest.model.AuthorDto;
import com.okay.jaspertest.model.BookDto;

import java.util.List;

public interface DummyService {

    List<BookDto> createDummyBookList(int size);

    List<AuthorDto> createDummyAuthorList(int size);

    List<List<Object>> createDummyList(int size);

}