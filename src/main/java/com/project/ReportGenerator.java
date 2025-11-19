package com.project;


public interface ReportGenerator {
    

    public default void generateReport()
    {
        system.out.println("this is a generic report");
    }

}