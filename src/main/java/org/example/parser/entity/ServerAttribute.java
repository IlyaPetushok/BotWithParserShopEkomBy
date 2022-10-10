package org.example.parser.entity;

import java.util.ArrayList;
import java.util.List;

public class ServerAttribute {
    private String name;
    private List<String> nameDownCatalog=new ArrayList<>();

    public ServerAttribute(String name,List<String> nameDownCatalog){
        this.name=name;
        this.nameDownCatalog=nameDownCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNameDownCatalog() {
        return nameDownCatalog;
    }

    public void setNameDownCatalog(List<String> nameDownCatalog) {
        this.nameDownCatalog = nameDownCatalog;
    }
}
