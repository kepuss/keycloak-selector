package com.identityfirst.condition;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class AuthNoteEqualsCondition implements ConditionalAuthenticator {
    public static final AuthNoteEqualsCondition SINGLETON = new AuthNoteEqualsCondition();


    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {

        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        String noteKey = config != null ? config.getConfig().get(AuthNoteEqualsConditionFactory.CONFIG_NOTE_KEY) : null;
        String expectedValue = config != null ? config.getConfig().get(AuthNoteEqualsConditionFactory.CONFIG_EXPECTED_VALUE) : null;

        String noteValue = context.getAuthenticationSession().getAuthNote(noteKey);
        return expectedValue.equals(noteValue);
    }

    @Override public void action(AuthenticationFlowContext context) {}
    @Override public boolean requiresUser() { return false; }
    @Override public boolean configuredFor(KeycloakSession session, org.keycloak.models.RealmModel realm, UserModel user) { return true; }
    @Override public void setRequiredActions(KeycloakSession session, org.keycloak.models.RealmModel realm, UserModel user) {}
    @Override public void close() {}
}
