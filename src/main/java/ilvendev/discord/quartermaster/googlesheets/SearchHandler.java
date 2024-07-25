package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;

import java.util.List;

public class SearchHandler {
    private static final String spreadsheetId = "1hi_Mrz1vTPcF6tLH_qPOWKrSXKzo5ZH6W9hMsP3J8QQ";

    public static String findColumnByName(String name, String searchRange) {
        int columnLetter = (int) 'A';
        try {
            Sheets service = Auth.createSheetsService("Column  finder");
            List<Object> values = service.spreadsheets().values()
                    .get(spreadsheetId, searchRange)
                    .execute()
                    .getValues()
                    .getFirst();

            for (int i = 0; i < values.size(); i++) {
                if (name.equals(values.get(i).toString())) {
                    return Character.toString(columnLetter + i);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create sheets service");
            e.printStackTrace();
        }

        return "No such column";
    }

    public static String findRowNumberByName(String name, String column) {
        String range = column + "2:" + column;
        try {
            Sheets service = Auth.createSheetsService("Row number finder");
            List<Object> values = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .setMajorDimension("COLUMNS")
                    .execute()
                    .getValues()
                    .getFirst();

            for (int i = 0; i < values.size(); i++) {
                if (name.equals(values.get(i).toString())) {
                    return Integer.toString(i+2);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create sheets service");
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        String column = findColumnByName("Imię i nazwisko", "A1:L1");
        System.out.println(column);
        String row = findRowNumberByName("Jacob Reglan", column);
        System.out.println(row);
    }
}
