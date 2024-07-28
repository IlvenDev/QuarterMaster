package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;

import java.util.List;

public class SearchHandler {
    public static String findRowNumberByName(String name, String range) {
        try {
            Sheets service = SheetsSetup.createSheetsService("Row number finder");
            List<Object> values = service.spreadsheets().values()
                    .get(SheetsSetup.getSheetValues().get("spreadsheet"), range)
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
