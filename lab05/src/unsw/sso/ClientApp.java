package unsw.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.sso.providers.LoginProvider;

public class ClientApp {

    // static class defining User 
    private static class User {
        private String email; // something to define the user by
        private List<Class<? extends LoginProvider>> providers = new ArrayList<>();

        public User(String email, Class<? extends LoginProvider> provider) {
            this.email = email;
            this.providers.add(provider);
        }
    }

    private Map<String, User> users = new HashMap<>();
    private List<LoginProvider> providers = new ArrayList<>();
    private final String name;

    public ClientApp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void registerProvider(LoginProvider p) {
        providers.add(p);
    }

    public <T extends LoginProvider> T getProvider(Class<T> p) {
        return providers.stream()
        .filter(x -> p.equals(x.getClass()))    // check for providers of the specified class
        .findFirst().map(x -> p.cast(x))        // find matching and cast it to the specified class
        .orElse(null);                          // return null if no provider is found
    }

    public boolean hasProvider(Class<? extends LoginProvider> p) {
        // check if client app has a provider of matching class 
        return providers.stream().anyMatch(x -> p.equals(x.getClass()));
    }

    public void registerUser(Token token, Class<? extends LoginProvider> p) {
        if (this.users.get(token.getUserEmail()) != null) {
            // user already exists --> add new provider
            this.users.get(token.getUserEmail()).providers.add(p);
        } else {
            // register the new user
            this.users.put(token.getUserEmail(), new User(token.getUserEmail(), p));
        }
    }

    public boolean hasUserForProvider(String email, LoginProvider p) {
        return hasProvider(p.getClass())            // check client app supports specified provider
                && this.users.get(email) != null    // check user of specified email is registered
                && this.users.get(email).providers.contains(p.getClass()); // check user has provider registered
    }
}
