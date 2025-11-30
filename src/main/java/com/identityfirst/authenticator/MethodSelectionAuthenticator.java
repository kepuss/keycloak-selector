package com.identityfirst.authenticator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class MethodSelectionAuthenticator implements Authenticator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        Map<String, Object> configData = loadJsonConfig(context);
        // Render a simple form with buttons/links to choose the method
        LoginFormsProvider forms = context.form();
        forms.setAttribute("config", configData);
        Response response = forms.createForm("method-selection.ftl");
        context.challenge(response);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String choice = formData.getFirst("method");

        if (choice == null || choice.isBlank()) {
            Response retry = context.form()
                    .setError("Messages.INVALID_LOGIN")
                    .createForm("method-selection.ftl");
            context.failureChallenge(org.keycloak.authentication.AuthenticationFlowError.INVALID_USER, retry);
            return;
        }

        // Save the choice in the AuthenticationSession for conditional subflows
        context.getAuthenticationSession().setAuthNote("chosen_method", choice);

        // Mark success so the flow continues
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession session,
                                 org.keycloak.models.RealmModel realm,
                                 UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession session,
                                   org.keycloak.models.RealmModel realm,
                                   UserModel user) {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    private Map<String, Object> loadJsonConfig(AuthenticationFlowContext context) {
        Map<String, Object> configData = new HashMap<>();
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();

        if (config != null) {
            String json = config.getConfig().get("configJson"); // key to store JSON
            if (json != null && !json.isBlank()) {
                try {
                    configData = MAPPER.readValue(json, Map.class);
                } catch (Exception e) {
                    // fallback: empty map
                    e.printStackTrace();
                }
            }
        }

        return configData;
    }
}
