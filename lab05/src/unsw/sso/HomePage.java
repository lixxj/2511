package unsw.sso;

public class HomePage extends Page {

    private Page previousState;

    public HomePage(Page previousState) {
        this.previousState = previousState;
    }

    @Override
    public Page interact(Object o) {
        return this;
    }

    @Override
    public Page back() {
        return this.previousState;
    }

    @Override
    public String getName() {
        return "Home";
    }
    
}
