package fr.korastrip.paperplugin;

import io.papermc.paper.util.Tick;

import java.time.Duration;

public class Utils
{

    public static int toTick(int seconds) {
        return Tick.tick().fromDuration(Duration.ofSeconds(seconds));
    }

}
