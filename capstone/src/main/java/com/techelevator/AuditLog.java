package com.techelevator;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditLog {
    public static void log(String message) {
        String dataFile = "C:\\Users\\johne\\OneDrive\\Desktop\\Capstone\\module-1-capstone\\capstone\\src\\main\\java\\com\\techelevator\\Log.txt";
        try (PrintWriter dataOutput = new PrintWriter(new FileOutputStream(dataFile, true))) {
            SimpleDateFormat formatter= new SimpleDateFormat("MM-dd-yyyy HH:mm:ss a");
            Date timeStamp = new Date(System.currentTimeMillis());
            dataOutput.println(formatter.format(timeStamp) + " " + message);
        } catch (Exception e) {
            System.out.println("An error has occurred.");
        }
    }
}
