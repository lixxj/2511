package unsw.sso;

import java.util.HashMap;
import java.util.Map;

public class Browser {
    
    private Map<ClientApp, Token> tokenMap = new HashMap<>();
    private ClientApp currentApp = null;
    private Page currentPage = null;

    public void visit(ClientApp app) {
        // set current app
        this.currentApp = app;

        // if token has already been validated for the current app, go to HomePage
        if (tokenMap.containsKey(app)) {
            this.currentPage = new HomePage(null);
        } else { // otherwise, go to SelectProvider
            this.currentPage = new SelectProvider(this.currentApp);
        }
    }

    public String getCurrentPageName() {
        // if currentPage has a name, return it (carefully consider the null page state)
        return this.currentPage != null ? currentPage.getName() : null;
    }

    public void clearCache() {
        this.tokenMap.clear(); // remove all mappings from this map
    } 

    public void interact(Object using) {
        // if current page is not a defined page state, return
        if (this.currentPage == null) return;

        if (using == null) { // go back
            this.currentPage = this.currentPage.back();
        } else { // interact with page

            // receiving a validated token --> add it to the token map
            if (using instanceof Token && ((Token)using).getAccessToken() != null) {
                this.tokenMap.put(currentApp, (Token)using);
            }

            // go ahead and interact with the current page state
            this.currentPage = this.currentPage.interact(using);
        }

    }
}
