package ilvendev.discord.quartermaster.userManagement;
import ilvendev.discord.quartermaster.googlesheets.SearchHandler;
import ilvendev.discord.quartermaster.googlesheets.SheetsSetup;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;

public class UserManager {
    private static final HashMap<String, String> users = new HashMap<>();
    public static void setupUsers(List<Member> members){
        String range = SheetsSetup.getSheetValues().get("rostersheet")+"!"+SheetsSetup.getSheetValues().get("idcolumn")+"2:"+SheetsSetup.getSheetValues().get("idcolumn");
        for(Member member : members){
            String rowNumber = SearchHandler.findRowNumberByName(member.getUser().getName(), range);
            if(!rowNumber.isEmpty()){
                users.put(member.getUser().getName(), rowNumber);
            }
        }
    }

    public static HashMap<String, String> getUsers(){
        return users;
    }
}
