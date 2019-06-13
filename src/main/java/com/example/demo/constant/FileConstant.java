package com.example.demo.constant;

public class FileConstant {

    public static final String CRT_FILENAME = "{owner}_tab_crt_{tableName}.sql";

    public static final String IDX_FILENAME = "{owner}_tab_idx_{tableName}.sql";

    public static final String SYN_FILENAME = "{owner}_tab_syn_{tableName}.sql";

    public static final String GRT_FILENAME = "{owner}_tab_grt_{tableName}.sql";

    public static final String TRI_FILENAME_BI = "{owner}_tab_tri_bi_{tableName}.sql";

    public static final String TRI_FILENAME_BU = "{owner}_tab_tri_bu_{tableName}.sql";

    private FileConstant() {
        throw new AssertionError("The Class cannot be instance....");
    }

}
