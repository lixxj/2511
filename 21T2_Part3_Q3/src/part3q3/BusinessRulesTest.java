package part3q3;

import java.util.HashMap;
import java.util.Map;

import part3q3.enums.BusinessRuleGroupTypes;
import part3q3.enums.BusinessRuleOperators;
import part3q3.enums.BusinessRuleTransformations;

public class BusinessRulesTest {
    public static void main(String[] args) {
        // this checks if the responses that the user has is greater than invites / 2
        BusinessRule hasResponses = BusinessRuleFactory.CreateComparisonOperator(
            BusinessRuleFactory.LookupVariable("responses"),
            BusinessRuleOperators.GREATER_THAN,
            BusinessRuleFactory.CreateTransformationOperator(
                BusinessRuleFactory.LookupVariable("invites"),
                BusinessRuleTransformations.DIVISION,
                BusinessRuleFactory.CreateConstant(2)
            )
        );

        // checks the user either has an email or a phone number
        BusinessRule hasEmailOrPhoneNumber = BusinessRuleFactory.CreateGroupOperator(
            BusinessRuleFactory.CreateComparisonOperator(
                BusinessRuleFactory.LookupVariable("email"),
                BusinessRuleOperators.IS_NOT_BLANK
            ),
            BusinessRuleGroupTypes.OR,
            BusinessRuleFactory.CreateComparisonOperator(
                BusinessRuleFactory.LookupVariable("phoneNumber"),
                BusinessRuleOperators.IS_NOT_BLANK
            )
        );

        // checks that the above two conditions are both true.
        BusinessRule hasResponsesAndEitherPhoneNumberOrEmail = BusinessRuleFactory
            .CreateGroupOperator(
                hasResponses,
                BusinessRuleGroupTypes.AND,
                hasEmailOrPhoneNumber
            );

        // personA has responses > invites / 2 but doesn't have either a PhoneNumber or an Email
        Map<String, Object> personA = new HashMap<>();
        personA.put("responses", 10);
        personA.put("invites", 10);

        assertFalse(hasResponsesAndEitherPhoneNumberOrEmail.evaluate(personA));

        // personB has a phone number but has not enough responses
        Map<String, Object> personB = new HashMap<>();
        personB.put("responses", 0);
        personB.put("invites", 5);
        personB.put("phoneNumber", "0482839292");

        assertFalse(hasResponsesAndEitherPhoneNumberOrEmail.evaluate(personB));

        // this user has enough responses, and a valid email
        Map<String, Object> personC = new HashMap<>();
        personC.put("responses", 105019);
        personC.put("invites", 105020);
        personC.put("email", "cs2511@cse.unsw.edu.au");

        assertTrue(hasResponsesAndEitherPhoneNumberOrEmail.evaluate(personC));
    }

    private static void assertTrue(boolean b) {
        if (!b) {
            throw new RuntimeException("Was expecting true got false");
        }
    }

    private static void assertFalse(boolean b) {
        if (b) {
            throw new RuntimeException("Was expecting false got true");
        }
    }
}
