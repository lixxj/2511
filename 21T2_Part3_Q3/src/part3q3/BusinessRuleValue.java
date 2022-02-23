package part3q3;

import java.util.Map;

public interface BusinessRuleValue {
    public Object evaluate(Map<String, Object> values);
}
