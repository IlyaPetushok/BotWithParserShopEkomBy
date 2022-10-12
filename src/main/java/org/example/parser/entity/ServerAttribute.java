package org.example.parser.entity;

import java.util.ArrayList;
import java.util.List;

public class ServerAttribute {
    private String name;
    private String href;
    private String nameCatalog;

    public String getNameCatalog() {
        return nameCatalog;
    }

    public void setNameCatalog(String nameCatalog) {
        this.nameCatalog = nameCatalog;
    }

    private List<String> nameDownCatalog=new ArrayList<>();

    public ServerAttribute(String name,List<String> nameDownCatalog){
        this.name=name;
        this.nameDownCatalog=nameDownCatalog;
    }

    public ServerAttribute(String nameCatalog,String name,String href){
        this.nameCatalog=nameCatalog;
        this.name=name;
        this.href=href;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
