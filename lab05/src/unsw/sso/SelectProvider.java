package unsw.sso;

import unsw.sso.providers.LoginProvider;

public class SelectProvider extends Page {

    private ClientApp app;

    public SelectProvider(ClientApp app) { this.app = app; }

    @Override
    public Page interact(Object o) {
        // check for login provider that is supported by client app --> go to the specified state
        if (o instanceof LoginProvider && this.app.hasProvider(((LoginProvider)o).getClass())) 
            return new LoginState<>(app, (LoginProvider) o);
        
        // do nothing
        return this;
    }

    @Override
    public Page back() {
        return null;
    }

    @Override
    public String getName() {
        return "Select a Provider";
    }
    
}
