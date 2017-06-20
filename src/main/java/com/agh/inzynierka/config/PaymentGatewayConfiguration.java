package com.agh.inzynierka.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class PaymentGatewayConfiguration {

    @NotEmpty
    private String mode = "sandbox";

    //FIXME generate the authorization parameters
    @NotEmpty
    private String clientId = "to be verified";

    @NotEmpty
    private String clientSecret = "to be verified";

    @NotEmpty
    private String merchantEmail = "p.przyha-facilitator@gmail.com";

    @JsonProperty
    public String getMode() {
        return mode;
    }

    @JsonProperty
    public void setMode(final String mode) {
        this.mode = mode;
    }

    @JsonProperty
    public String getClientId() {
        return clientId;
    }

    @JsonProperty
    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty
    public String getClientSecret() {
        return clientSecret;
    }

    @JsonProperty
    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @JsonProperty
    public String getMerchantEmail() {
        return merchantEmail;
    }

    @JsonProperty
    public void setMerchantEmail(final String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }
}