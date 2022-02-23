package part3q3.composite;

import java.util.Map;

import part3q3.BusinessRule;

public class And implements BusinessRule {
    
    private BusinessRule rule1;
    private BusinessRule rule2;
    
    public And(BusinessRule rule1, BusinessRule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        return rule1.evaluate(values) && rule2.evaluate(values);
    }
}
