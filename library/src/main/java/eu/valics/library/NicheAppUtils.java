package eu.valics.library;

/**
 * Created by L on 9/7/2016.
 */
public class NicheAppUtils {

    // Ad ids

    private static String INTERSTITIAL_AD_ID;
    private static String BANNER_ID;

    // this is for floatingIcon
    private static int FLOATING_ICON_ID;
    private static String FLOATING_NOTIFICATION_TITLE;
    private static String FLOATING_NOTIFICATION_DESCRIPTION;
    private static String FLOATING_SERVICE_RESTART_PHRASE;

    public static void initAds(String interstitialId, String bannerId){
        INTERSTITIAL_AD_ID = interstitialId;
        BANNER_ID = bannerId;
    }

    public static void initFloatingIcon(int floatingIconId,
                                        String title,
                                        String description,
                                        String restartPhrase){
        FLOATING_ICON_ID = floatingIconId;
        FLOATING_NOTIFICATION_TITLE = title;
        FLOATING_NOTIFICATION_DESCRIPTION = description;
        FLOATING_SERVICE_RESTART_PHRASE = restartPhrase;
    }

    public static String getInterstitialAdId() {
        return INTERSTITIAL_AD_ID;
    }

    public static String getBannerId() {
        return BANNER_ID;
    }

    public static int getFloatingIconId() {
        return FLOATING_ICON_ID;
    }

    public static String getFloatingNotificationTitle() {
        return FLOATING_NOTIFICATION_TITLE;
    }

    public static String getFloatingNotificationDescription() {
        return FLOATING_NOTIFICATION_DESCRIPTION;
    }

    public static String getFloatingServiceRestartPhrase() {
        return FLOATING_SERVICE_RESTART_PHRASE;
    }
}
