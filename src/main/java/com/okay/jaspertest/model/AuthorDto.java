package com.okay.jaspertest.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class AuthorDto implements Serializable {

    private String name;

    private String description;

}