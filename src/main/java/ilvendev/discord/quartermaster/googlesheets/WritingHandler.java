package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class WritingHandler { ;
    public static void updateSheet(List<Object> newValue, String range) throws IOException {
        ValueRange valueRange = new ValueRange()
                .setValues(Collections.singletonList(newValue));
        try {
            Sheets service = SheetsSetup.createSheetsService("Writer");
            service.spreadsheets().values().update(SheetsSetup.getSpreadsheetId(), range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (Exception e) {
            System.out.println("Could not create sheets service");
            e.printStackTrace();
        }
    }
}