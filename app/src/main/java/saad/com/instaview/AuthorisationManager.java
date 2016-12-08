package saad.com.instaview;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AuthorisationManager {

    /**
     *
     * @param context
     */
    public static void startAuthorisation(Context context) {
        String authUrl = InstaViewConsts.AUTHORIZATION_URL
                + "/?client_id=" + InstaViewConsts.CLIENT_ID
                + "&redirect_uri=" + InstaViewConsts.REDIRECT_URI
                + "&response_type=token"
                + "&scope=" + InstaViewConsts.SCOPE;

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
