package org.api.model.playlist;

import lombok.Data;

import java.util.List;

@Data
public class ArItem {
    private String name;
    private List<Object> tns;
    private List<Object> alias;
    private int id;
}