package ua.goit;

import java.time.ZoneId;
import java.util.Set;

public class Main {
    public static void main(String[] arg){

//        Timezone.getAvailableIDs timezone;
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        boolean b = availableZoneIds.contains("GMT+3");
        System.out.println(b);

    }
}
