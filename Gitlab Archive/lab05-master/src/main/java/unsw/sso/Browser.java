package unsw.sso;

import java.util.HashMap;
import java.util.Map;

import unsw.sso.providers.Hoogle;
import unsw.sso.providers.LinkedOut;

public class Browser {
    private Token currentToken = null;
    private String currentPage = null;
    private String previousPage = null;
    private ClientApp currentApp = null;
    
    private Map<ClientApp, Token> tokenCache = new HashMap<>();

    public void visit(ClientApp app) {
        this.previousPage = null;
        this.currentApp = app;
        this.currentToken = null;

        if (tokenCache.containsKey(app)) { // login by cached token
            this.currentPage = "Home";
        }
        else { // no cache
            this.currentPage = "Select a Provider";
        }

    }

    public String getCurrentPageName() {
        return this.currentPage;
    }

    public void clearCache() {
        this.tokenCache.clear();
    }

    public void interact(Object using) {
        if (using == null) {
            this.currentPage = this.previousPage;
            return;
        }

        switch (currentPage) {
            case "Select a Provider": {
                // if the currentApp doesn't have hoogle
                // then it has no providers, which just will prevent
                // transition.
                if (using instanceof Hoogle && currentApp.hasHoogle()) {
                    this.previousPage = currentPage;
                    this.currentPage = "Hoogle Login";
                } else if (using instanceof LinkedOut && currentApp.hasLinkedOut()) {
                    this.previousPage = currentPage;
                    this.currentPage = "LinkedOut Login";
                    // do nothing...
                }
                break;
            }
            case "Hoogle Login": {
                if (using instanceof Token) {
                    Token token = (Token) using;
                    if (token.getAccessToken() != null) {
                        this.previousPage = currentPage;
                        this.currentPage = "Home";
    
                        this.currentToken = token;
                        this.currentApp.registerUser((Token)token);

                        // add token to cache
                        this.tokenCache.put(currentApp, (Token)using);

                    } else {
                        // If accessToken is null, then the user is not authenticated
                        // Go back to select providers page
                        this.currentPage = "Select a Provider";
                    }
                } else {
                    // do nothing...
                }

                break;
            }
            case "LinkedOut Login": {
                if (using instanceof Token) {
                    Token token = (Token) using;
                    if (token.getAccessToken() != null) {
                        this.previousPage = currentPage;
                        this.currentPage = "Home";
    
                        this.currentToken = token;
                        this.currentApp.registerUser((Token)token);

                        // add token to cache
                        this.tokenCache.put(currentApp, (Token)using);

                    } else {
                        // If accessToken is null, then the user is not authenticated
                        // Go back to select providers page
                        this.currentPage = "Select a Provider";
                    }
                } else {
                    // do nothing...
                }

                break;
            }
            case "Home": {
                // no need to do anything
                break;
            }
        }
    }
}
