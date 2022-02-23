package part3q3.composite;

import java.util.Map;

import part3q3.BusinessRuleValue;

public class Lookup implements BusinessRuleValue {

    private String key;

    public Lookup(String key) {
        this.key = key;
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return values.get(key); // null if key doesn't exist
    }
    
}
