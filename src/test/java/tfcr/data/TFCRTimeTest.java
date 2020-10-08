package tfcr.data;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TFCRTimeTest {

    @Test
    public void testGetRawTime() {
    }

    @Test
    public void testGetDay() {
    }

    @Test
    public void testGetTimeOfYear() {
        // Some basic tests
        TFCRTime.time = 10_000;
        assertEquals(TFCRTime.getTimeOfYear(), 0.0049603174603175, 0.0001);

        TFCRTime.time = 100_000;
        assertEquals(TFCRTime.getTimeOfYear(), 0.049603174603175, 0.0001);

        // Test that we wrap over 1 properly
        TFCRTime.time = 3_000_000;
        assertEquals(TFCRTime.getTimeOfYear(), 0.488095238095238, 0.0001);

        // Test that negative values work fine
        TFCRTime.time = -3_000_000;
        assertEquals(TFCRTime.getTimeOfYear(), 0.511904761904762, 0.0001);
    }

    @Test
    public void testGetSeason() {
    }
}