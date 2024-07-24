package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;
import java.util.Objects;

public class SearchHandler {

    public static int findColumnNumberByName(String name, String searchRange) throws Exception {
        Sheets service = null;
        String spreadsheetId = "1hi_Mrz1vTPcF6tLH_qPOWKrSXKzo5ZH6W9hMsP3J8QQ";
        try {
            service = Auth.createSheetsService("Column number finder");

            List<Object> values = service.spreadsheets().values()
                    .get(spreadsheetId, searchRange)
                    .execute()
                    .getValues()
                    .getFirst();

            for (int i = 0; i < values.size(); i++) {
                if (name.equals(values.get(i).toString())) {
                    return i;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create sheets service");
        }

        return -1;
    };

    public static void main(String[] args) throws Exception {
        int columnNumber = findColumnNumberByName("Imię i nazwisko", "A1:L1");
        System.out.println(columnNumber);
    }
}
