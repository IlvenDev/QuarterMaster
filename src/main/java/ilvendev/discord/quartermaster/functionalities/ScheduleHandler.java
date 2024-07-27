package ilvendev.discord.quartermaster.functionalities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.LinkedHashMap;

public class ScheduleHandler {

    private static LinkedHashMap<String,String> daysInformations = new LinkedHashMap<>(){{
        put("poniedziałek", "desc");
        put("wtorek", "desc");
        put("środa"," desc");
        put("czwartek", "desc");
        put("piątek", "desc");
        put("sobota", "desc");
        put("niedziela", "desc");
    }};

    public static void updateDay(Message message, String day, String newDesc){
        daysInformations.put(day.toLowerCase(), newDesc);
        MessageEmbed newMessage = createScheduleEmbed();
        message.editMessageEmbeds(newMessage).queue();
    }

    public static MessageEmbed createScheduleEmbed(){
        EmbedBuilder embed = new EmbedBuilder();
        daysInformations.forEach((day, description) -> {embed.addField(day.toUpperCase(), description, false);});
        embed.setColor(0xdb0404);
        return embed.build();
    }

}
