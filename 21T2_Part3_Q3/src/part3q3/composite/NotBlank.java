package part3q3.composite;

import java.util.Map;

import part3q3.BusinessRule;
import part3q3.BusinessRuleValue;

public class NotBlank implements BusinessRule {

    private BusinessRuleValue value;

    public NotBlank(BusinessRuleValue value) {
        this.value = value;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        Object v = value.evaluate(values);
        if (v == null) return false;
        return !((String)v).isBlank();
    }

}
