package com.okay.jaspertest.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class BookDto implements Serializable {

    private String name;

    private String description;

}