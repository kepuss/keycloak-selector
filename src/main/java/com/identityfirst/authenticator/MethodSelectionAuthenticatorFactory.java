package com.identityfirst.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class MethodSelectionAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "method-selection-authenticator";
    private static final MethodSelectionAuthenticator SINGLETON = new MethodSelectionAuthenticator();
    private static final List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<>();

    static {
        // JSON configuration field
        ProviderConfigProperty jsonConfig = new ProviderConfigProperty();
        jsonConfig.setName("configJson");
        jsonConfig.setLabel("Selector JSON Configuration");
        jsonConfig.setHelpText("Provide JSON configuration for the selector.");
        jsonConfig.setType(ProviderConfigProperty.SCRIPT_TYPE);
        CONFIG_PROPERTIES.add(jsonConfig);
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {
        // No initialization needed
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // No post-initialization needed
    }

    @Override
    public void close() {
        // No cleanup needed
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Keycloak Selector";
    }

    @Override
    public String getHelpText() {
        return "Displays a screen where the user can choose a next step.";
    }

    @Override
    public String getReferenceCategory() {
        return "method-selection";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }
}
