package part3q3;

import part3q3.composite.And;
import part3q3.composite.Constant;
import part3q3.composite.Divide;
import part3q3.composite.GreaterThan;
import part3q3.composite.Lookup;
import part3q3.composite.Multiply;
import part3q3.composite.NotBlank;
import part3q3.composite.Or;
import part3q3.enums.BusinessRuleGroupTypes;
import part3q3.enums.BusinessRuleOperators;
import part3q3.enums.BusinessRuleTransformations;

public class BusinessRuleFactory {
    public static BusinessRule CreateGroupOperator(BusinessRule ruleA, BusinessRuleGroupTypes operator, BusinessRule ruleB) {
        switch(operator) {
            case AND:
                return new And(ruleA, ruleB);
            case OR:
                return new Or(ruleA, ruleB);
            default:
                return null;
        }
    }

    public static BusinessRule CreateComparisonOperator(BusinessRuleValue target, BusinessRuleOperators operator, BusinessRuleValue argument) {
        switch(operator) {
            case GREATER_THAN:
                return new GreaterThan(target, argument);
            default:
                return null;
        }
    }

    public static BusinessRule CreateComparisonOperator(BusinessRuleValue target, BusinessRuleOperators operator) {
        switch(operator) {
            case IS_NOT_BLANK:
                return new NotBlank(target);
            default:
                return null;
        }
    }

    public static BusinessRuleValue CreateTransformationOperator(BusinessRuleValue target, BusinessRuleTransformations transform, BusinessRuleValue argument) {
        switch(transform) {
            case DIVISION:
                return new Divide(target, argument);
            case MULTIPLICATION:
                return new Multiply(target, argument);
            default:
                return null;
        }
    }

    public static BusinessRuleValue LookupVariable(String value) {
        return new Lookup(value);
    }

    public static BusinessRuleValue CreateConstant(Object value) {
        return new Constant(value);
    }
}
