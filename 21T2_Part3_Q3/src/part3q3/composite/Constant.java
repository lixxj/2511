package part3q3.composite;

import java.util.Map;

import part3q3.BusinessRuleValue;

public class Constant implements BusinessRuleValue {

    private Object value;

    public Constant(Object value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return value;
    }
    
}
