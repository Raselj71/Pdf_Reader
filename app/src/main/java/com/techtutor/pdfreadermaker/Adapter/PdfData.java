package com.techtutor.pdfreadermaker.Adapter;





public class PdfData {

    private String name;


    private String size;


    private String date;


    private String path;

    public PdfData(String name, String size, String date, String path) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.path=path;

    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
