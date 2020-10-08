package tfcr.data;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class TFCRTime {
    public static long time;

    // Note that a vanilla minecraft day is 24,000 ticks = 20 minutes.
    public static final int TICKS_PER_DAY = 24_000;

    public static final int DAYS_PER_SEASON = 21;
    public static final int DAYS_PER_YEAR = DAYS_PER_SEASON * 4;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onServerWorldTick(TickEvent.WorldTickEvent event) {
        // On the start of every server tick, we update our local copy of the world's time.
        if (event.phase == TickEvent.Phase.START) {
            TFCRTime.time = event.world.getWorldInfo().getGameTime();
//            System.out.println("Ticks: " + time + " Season: " + getSeason() + " Day: " + getDay());
        }
    }

    /**
     * @return The age of the world in ticks.
     */
    public static long getRawTime() {
        return time;
    }

    /**
     * @return The age of the world in days.
     */
    public static long getDay() {
        return time / TICKS_PER_DAY;
    }

    /**
     * @return A float in [0, 1] representing the progress through this day.
     */
    public static float getTimeOfDay() {
        double rawTimeOfDay = (double)time / TICKS_PER_DAY;
        return (float)(rawTimeOfDay - Math.floor(rawTimeOfDay));
    }

    /**
     * @return A float in [0, 1] representing the progress in this year.
     */
    public static float getTimeOfYear() {
        double rawTimeOfYear = (double)time / (TICKS_PER_DAY * DAYS_PER_YEAR);
        return (float)(rawTimeOfYear - Math.floor(rawTimeOfYear));
    }

    /**
     * @return The current world's season.
     */
    public static Season getSeason() {
        long rawSeason = getDay() / DAYS_PER_SEASON;
        return Season.values()[(int) (rawSeason % 4)];
    }


}
