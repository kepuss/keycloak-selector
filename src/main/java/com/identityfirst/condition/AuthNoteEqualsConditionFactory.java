package com.identityfirst.condition;

import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class AuthNoteEqualsConditionFactory implements ConditionalAuthenticatorFactory {

    public static final String PROVIDER_ID = "auth-note-equals-condition";
    public static final String CONFIG_NOTE_KEY = "noteKey";
    public static final String CONFIG_EXPECTED_VALUE = "expectedValue";

    @Override public void init(org.keycloak.Config.Scope config) { }
    @Override public void postInit(KeycloakSessionFactory factory) { }
    @Override public void close() { }

    @Override public String getId() { return PROVIDER_ID; }
    @Override public String getDisplayType() { return "Condition - Auth Note Equals"; }
    @Override public String getHelpText() { return "Executes subflow if auth note equals expected value (configurable)."; }
    @Override
    public String getReferenceCategory() {
        return "condition";
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return AuthNoteEqualsCondition.SINGLETON;
    }

    @Override public boolean isConfigurable() { return true; }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() { return false; }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> config = new ArrayList<>();

        ProviderConfigProperty noteKeyProp = new ProviderConfigProperty();
        noteKeyProp.setName("noteKey");
        noteKeyProp.setLabel("Auth Note Key");
        noteKeyProp.setType(ProviderConfigProperty.STRING_TYPE);
        noteKeyProp.setHelpText("The AuthenticationSession note key to check.");
        config.add(noteKeyProp);

        ProviderConfigProperty expectedValueProp = new ProviderConfigProperty();
        expectedValueProp.setName("expectedValue");
        expectedValueProp.setLabel("Expected Value");
        expectedValueProp.setType(ProviderConfigProperty.STRING_TYPE);
        expectedValueProp.setHelpText("The expected value of the note to execute the subflow.");
        config.add(expectedValueProp);

        return config;
    }
}
