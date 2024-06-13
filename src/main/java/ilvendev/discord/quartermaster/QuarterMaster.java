package ilvendev.discord.quartermaster;

import ilvendev.discord.quartermaster.commands.CommandManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class QuarterMaster {

    private final Dotenv config;
    private final ShardManager shardManager;

    public QuarterMaster() throws InvalidTokenException{
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Quarter Master"));
        shardManager = builder.build();

        shardManager.addEventListener(new CommandManager());
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) {
        System.out.println();
        try {
            QuarterMaster bot = new QuarterMaster();
        } catch (InvalidTokenException e) {
            System.out.println("BOT Token invalid");
        }
    }
}
