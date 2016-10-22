package saad.com.instaview;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;

/**
 * Created by kalsa12 on 2016-10-20.
 */

public class MyInstagram {

    private static Instagram instagram;

    private MyInstagram() {
    }

    public static Instagram init(Token accessToken){
        if( instagram == null ){
            instagram = new Instagram(accessToken);
        }
        return instagram;
    }

    public static Instagram getInstance(){
        return instagram;
    }

    public boolean isInitialised(){
        return instagram != null;
    }
}
