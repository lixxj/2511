package unsw.sso;

import unsw.sso.providers.LoginProvider;

public class LoginState<T extends LoginProvider> extends Page {

    private ClientApp app;
    private T provider;

    public LoginState(ClientApp app, T provider) {
        this.app = app;
        this.provider = provider;
    }

    @Override 
    public Page interact(Object o) {
        // i.e. Hoogle can only interact with tokens and Instaham with strings (and tokens too)
        if (!this.provider.validateLogin(o)) return back();

        if (o instanceof Token) {
            Token tkn = (Token) o;

            // check token provider is the same as the provider for the current login provider state
            if (!tkn.getProviderName().equals(this.provider.getClass().getSimpleName())) return back();

            // register the user
            app.registerUser(tkn, provider.getClass());

            // invalid token
            if (tkn.getAccessToken() == null) {
                return back(); // logout/other
            }

            return new HomePage(this);
        }

        return this;
    }

    @Override
    public Page back() {
        return new SelectProvider(app);
    }

    @Override
    public String getName() {
        // cheat codes ... 
        return this.provider.getClass().getSimpleName() + " Login";
    }

}
