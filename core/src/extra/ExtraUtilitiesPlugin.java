package extra;

import arc.Events;
import arc.func.Cons;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Timer;
import arc.util.CommandHandler.CommandRunner;
import mindustry.mod.*;
import mindustry.game.EventType;
import mindustry.core.GameState;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Administration;
import static mindustry.Vars.player;
import static mindustry.Vars.state;
import static mindustry.Vars.netServer;
import static mindustry.Vars.netClient;


public class ExtraUtilitiesPlugin extends mindustry.mod.Plugin {

    public ExtraUtilitiesPlugin() {
        Log.info("|--> Extra-Utilities is loading...");
    }

    public void init(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            if (state.serverPaused == false && Groups.player.size() == 0) {
                state.serverPaused = true;
                Log.info("The server was automatically paused");
                Call.sendMessage("[Auto-Pause] [green]ON");
            }
        });
        Events.on(EventType.PlayerJoin.class, e -> {
            if (state.serverPaused == true && Groups.player.size() == 1) {
                state.serverPaused = false;
                Log.info("The server was automatically unpaused");
                Call.sendMessage("[Auto-Pause] [red]OFF");
            }
        });
        Events.on(EventType.PlayerLeave.class, e -> {
            if (state.serverPaused == false && Groups.player.size()-1 == 0) {
                state.serverPaused = true;
                Log.info("The server was automatically paused");
                Call.sendMessage("[Auto-Pause] [green]ON");
            }
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("pause", "<on/off>", "Pause/Unpause the game.", (arg, player) -> {
            if (arg[0].equals("on") && state.serverPaused == false) {
                if (state.serverPaused == false) {
                    state.serverPaused = true;
                    Call.sendMessage("[Pause] [green]ON");
                }
            }
            else {
                player.sendMessage("[scarlet]Server is already paused.");
            }
            if (arg[0].equals("off")) {
                if (state.serverPaused == true) {
                    state.serverPaused = false;
                    Call.sendMessage("[Pause] [red]OFF");
                }
            }
            else {
                player.sendMessage("[scarlet]Server is already unpaused.");
            }
        });
    }
}
