package saad.com.instaview;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AuthorisationManager {
    private static final String AUTHORIZATION_URL = "https://api.instagram.com/oauth/authorize";
    private static final String CLIENT_ID = "6d531664f106445e9a4af197073f99e9";
    private static final String CLIENT_SECRET = "289550a757e9455a82b09ebd148f954f";
    public static final String REDIRECT_URI = "https://skalim.github.io/instaview";
    private static final String SCOPE = "public_content";

    /**
     *
     * @param context
     */
    public static void startAuthorisation(Context context) {
        String authUrl = AUTHORIZATION_URL
                + "/?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=token"
                + "&scope=" + SCOPE;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(authUrl));
        context.startActivity(i);

        /* TODO wait for custom tab not redirect to app bug fix
        AppAuthConfiguration appAuthConfig = new AppAuthConfiguration.Builder()
                .setBrowserMatcher(new BrowserWhitelist(
                        VersionedBrowserMatcher.CHROME_CUSTOM_TAB))
                .build();

        AuthorizationServiceConfiguration config =
                new AuthorizationServiceConfiguration(Uri.parse(AUTHORIZATION_URL),
                        Uri.parse("https://https://api.instagram.com/oauth/authorize"),
                        null);
        AuthorizationRequest authorizationRequest = new AuthorizationRequest.Builder(
                config,
                CLIENT_ID,
                ResponseTypeValues.TOKEN,
                Uri.parse(REDIRECT_URI))
                .setScope(SCOPE)
                .setCodeVerifier(null)
                .build();
        AuthorizationService authorizationService = new AuthorizationService(context, appAuthConfig);
        Intent postAuthIntent = new Intent(context, successActivity);
        Intent authCanceledIntent = new Intent(context, cancelledActivity);
        authorizationService.performAuthorizationRequest(
                authorizationRequest,
                PendingIntent.getActivity(context, authorizationRequest.hashCode(), postAuthIntent, 0),
                PendingIntent.getActivity(context, authorizationRequest.hashCode(), authCanceledIntent, 0));
                */
    }
}
