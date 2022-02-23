package part3q3.composite;

import java.util.Map;

import part3q3.BusinessRule;
import part3q3.BusinessRuleException;
import part3q3.BusinessRuleValue;

public class GreaterThan implements BusinessRule {

    private BusinessRuleValue value1;
    private BusinessRuleValue value2;


    public GreaterThan(BusinessRuleValue value1, BusinessRuleValue value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        if (value1 == null || value2 == null) throwException();

        Object v1 = value1.evaluate(values);
        Object v2 = value2.evaluate(values);

        if (!(v1 instanceof Number && v2 instanceof Number)) throwException();

        return (Integer) value1.evaluate(values) > (Integer) value2.evaluate(values);
    }

    private void throwException() {
        throw new BusinessRuleException("Both arguments have to be numeric");
    }
    
}
