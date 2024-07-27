package ilvendev.discord.quartermaster.googlesheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import ilvendev.discord.quartermaster.userManagement.UserManager;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WritingHandler { ;
    public static void updateSheet(List<Object> newValue, String range) {
        ValueRange valueRange = new ValueRange()
                .setValues(Collections.singletonList(newValue));
        try {
            Sheets service = SheetsSetup.createSheetsService("Writer");
            service.spreadsheets().values().update(SheetsSetup.getSheetValues().get("spreadsheetid"), range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (Exception e) {
            System.out.println("Could not create sheets service");
            e.printStackTrace();
        }
    }

    public static boolean writeArrestToRoster(List<ModalMapping> modalValues, int currentBlankRowNumber){
        String range = SheetsSetup.getSheetValues().get("arrestsheet")+"!A"+currentBlankRowNumber+":E"+currentBlankRowNumber;
        List<Object> newValues = new ArrayList<>();
        for (ModalMapping modalValue : modalValues) {
            newValues.add(modalValue.getAsString());
        }
        try {
            WritingHandler.updateSheet(newValues, range);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeRankToRoster(String username, List<ModalMapping> modalValues){
        String rankColumn = SheetsSetup.getSheetValues().get("rankcolumn");
        String row = UserManager.getUsers().get(username);
        String cell = SheetsSetup.getSheetValues().get("rostersheet")+"!"+rankColumn+row+":"+rankColumn+row;
        try {
            WritingHandler.updateSheet(List.of(modalValues.get(1).getAsString()), cell);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeExcuseToRoster(String username, List<ModalMapping> modalValues){
        String row = UserManager.getUsers().get(username);
        String excuseColumn = SheetsSetup.getSheetValues().get("excusecolumn");
        String cell = SheetsSetup.getSheetValues().get("rostersheet")+"!"+excuseColumn+row+":"+excuseColumn+row;
        try {
            WritingHandler.updateSheet(List.of(modalValues.getFirst().getAsString()), cell);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}