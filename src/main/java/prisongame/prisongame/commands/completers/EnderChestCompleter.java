package prisongame.prisongame.commands.completers;

public class EnderChestCompleter extends SubcommandCompleter {
    public EnderChestCompleter() {
        super("enderchest", new String[] {
                "inspect",
                "clear"
        });
    }
}
