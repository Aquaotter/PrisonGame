package prisongame.prisongame.commands.completers;

public class InvcommandCompleter extends SubcommandCompleter {
    public InvcommandCompleter() {
        super("inv", new String[] {
                "inspect",
                "clear"
        });
    }
}
