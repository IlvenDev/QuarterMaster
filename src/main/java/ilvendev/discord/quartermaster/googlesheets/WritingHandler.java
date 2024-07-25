package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.protobuf.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WritingHandler {
    private static final String spreadsheetId = "1hi_Mrz1vTPcF6tLH_qPOWKrSXKzo5ZH6W9hMsP3J8QQ";
    private static String column = SearchHandler.findColumnByName("Imię i nazwisko", "A1:L1");
    private static String row = SearchHandler.findRowNumberByName("Jacob Reglan", column);
    private static String range = column+row;

    public static void updateSheet(String newValue) throws IOException {
        List<Object> values = List.of(newValue);
        ValueRange valueRange = new ValueRange()
                .setValues(Collections.singletonList(values));
        try {
            Sheets service = Auth.createSheetsService("Writer");
            service.spreadsheets().values().update(spreadsheetId, range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (Exception e) {
            System.out.println("Could not create sheets service");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            updateSheet("Harry Harson");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}