package unsw.sso.providers;

import unsw.sso.Page;
import unsw.sso.Token;

public abstract class LoginProvider {

    public boolean validateLogin(Object o) {
        return o instanceof Token;
    }    
}
