package ilvendev.discord.quartermaster.userManagement;
import ilvendev.discord.quartermaster.googlesheets.SearchHandler;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;


public class UserManager {
    private static final HashMap<String, String> users = new HashMap<String , String>();

    public static void setupUsers(List<Member> members){
        String column = SearchHandler.findColumnByName("Nick DC", "A1:L1");
        for(Member member : members){
            String rowNumber = SearchHandler.findRowNumberByName(member.getUser().getName(), column);
            if(!rowNumber.isEmpty()){
                users.put(member.getUser().getName(), rowNumber);
            }
        }
        System.out.println(users);
    }
}
