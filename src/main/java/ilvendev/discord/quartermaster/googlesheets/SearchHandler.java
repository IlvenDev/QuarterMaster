package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;

import java.util.List;

public class SearchHandler {
    public static String findColumnByName(String name, String searchRange) {
        int columnLetter = (int) 'A';
        try {
            Sheets service = SheetsSetup.createSheetsService("Column  finder");
            List<Object> values = service.spreadsheets().values()
                    .get(SheetsSetup.getSpreadsheetId(), searchRange)
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
            Sheets service = SheetsSetup.createSheetsService("Row number finder");
            List<Object> values = service.spreadsheets().values()
                    .get(SheetsSetup.getSpreadsheetId(), range)
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
}
