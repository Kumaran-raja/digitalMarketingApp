package com.example.kumaranraja.business;

public class TableRowData {
    private String column1Data;
    private String column2Data;
    private String column3Data;
    private String column4Data;

    public TableRowData(String column1Data, String column2Data, String column3Data, String column4Data) {
        this.column1Data = column1Data;
        this.column2Data = column2Data;
        this.column3Data = column3Data;
        this.column4Data = column4Data;
    }

    public String getColumn1Data() {
        return column1Data;
    }

    public String getColumn2Data() {
        return column2Data;
    }

    public String getColumn3Data() {
        return column3Data;
    }

    public String getColumn4Data() {
        return column4Data;
    }
}