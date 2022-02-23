package part3q3;

import java.util.Map;

public interface BusinessRule {
    public boolean evaluate(Map<String, Object> values);
}
