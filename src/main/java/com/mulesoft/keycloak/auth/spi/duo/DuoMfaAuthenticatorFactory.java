/*
Copyright 2018 MuleSoft, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mulesoft.keycloak.auth.spi.duo;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class DuoMfaAuthenticatorFactory implements AuthenticatorFactory{
    public static final String PROVIDER_ID = "duo-mfa-authenticator";
    private static final DuoMfaAuthenticator SINGLETON = new DuoMfaAuthenticator();
    public static final String PROP_IKEY = "duomfa.ikey";
    public static final String PROP_SKEY = "duomfa.skey";
    public static final String PROP_AKEY = "duomfa.akey";
    public static final String PROP_APIHOST = "duomfa.apihost";
    public static final String PROP_GROUPS = "duomfa.groups";
    public static final String PROP_USERNAME = "duomfa.username";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean  isUserSetupAllowed() {
        return false;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty ikey = new ProviderConfigProperty();
        ikey.setName(PROP_IKEY);
        ikey.setLabel("Duo WebSDK ikey");
        ikey.setType(ProviderConfigProperty.STRING_TYPE);
        ikey.setHelpText("Integration key from Duo admin portal");
        configProperties.add(ikey);

        ProviderConfigProperty skey = new ProviderConfigProperty();
        skey.setName(PROP_SKEY);
        skey.setLabel("Duo WebSDK skey");
        skey.setType(ProviderConfigProperty.STRING_TYPE);
        skey.setHelpText("Secret key from Duo admin portal");
        configProperties.add(skey);

        ProviderConfigProperty akey = new ProviderConfigProperty();
        akey.setName(PROP_AKEY);
        akey.setLabel("Challenge nonce");
        akey.setType(ProviderConfigProperty.STRING_TYPE);
        akey.setHelpText("Any random alpha-numeric string, 40 characters or more. You can generate this yourself");
        configProperties.add(akey);

        ProviderConfigProperty api_host = new ProviderConfigProperty();
        api_host.setName(PROP_APIHOST);
        api_host.setLabel("Duo WebSDK API host");
        api_host.setType(ProviderConfigProperty.STRING_TYPE);
        api_host.setHelpText("API hostname from Duo admin portal");
        configProperties.add(api_host);

        ProviderConfigProperty groups = new ProviderConfigProperty();
        groups.setName(PROP_GROUPS);
        groups.setLabel("Groups that require Duo");
        groups.setType(ProviderConfigProperty.STRING_TYPE);
        groups.setHelpText("Comma separated list of groups that require Duo (optional)");
        configProperties.add(groups);

        ProviderConfigProperty username = new ProviderConfigProperty();
        username.setName(PROP_USERNAME);
        username.setLabel("Username attribute");
        username.setType(ProviderConfigProperty.STRING_TYPE);
        username.setHelpText("Name of the user model's alternative attribute which will be presented to duo as the username (optional, using 'username' by default)");
        configProperties.add(username);
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getHelpText() {
        return "MFA provided by Duo Security";
    }

    @Override
    public String getDisplayType() {
        return "Duo MFA";
    }

    @Override
    public String getReferenceCategory() {
        return "MFA";
    }

    @Override
    public void init(Config.Scope config) {}

    @Override
    public void postInit(KeycloakSessionFactory factory) {}

    @Override
    public void close() {}

}
