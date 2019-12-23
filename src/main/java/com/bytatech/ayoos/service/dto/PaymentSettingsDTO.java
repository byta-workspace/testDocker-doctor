package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PaymentSettings entity.
 */
public class PaymentSettingsDTO implements Serializable {

    private Long id;

    private Boolean isPaymentEnabled;

    private Double amount;

    private String paymentMethod;

    private String currency;

    private String intent;

    private String noteToPayer;

    private String paymentGatewayProvider;

    private String paymentGatewayCredentials;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPaymentEnabled() {
        return isPaymentEnabled;
    }

    public void setIsPaymentEnabled(Boolean isPaymentEnabled) {
        this.isPaymentEnabled = isPaymentEnabled;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getNoteToPayer() {
        return noteToPayer;
    }

    public void setNoteToPayer(String noteToPayer) {
        this.noteToPayer = noteToPayer;
    }

    public String getPaymentGatewayProvider() {
        return paymentGatewayProvider;
    }

    public void setPaymentGatewayProvider(String paymentGatewayProvider) {
        this.paymentGatewayProvider = paymentGatewayProvider;
    }

    public String getPaymentGatewayCredentials() {
        return paymentGatewayCredentials;
    }

    public void setPaymentGatewayCredentials(String paymentGatewayCredentials) {
        this.paymentGatewayCredentials = paymentGatewayCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentSettingsDTO paymentSettingsDTO = (PaymentSettingsDTO) o;
        if (paymentSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentSettingsDTO{" +
            "id=" + getId() +
            ", isPaymentEnabled='" + isIsPaymentEnabled() + "'" +
            ", amount=" + getAmount() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", intent='" + getIntent() + "'" +
            ", noteToPayer='" + getNoteToPayer() + "'" +
            ", paymentGatewayProvider='" + getPaymentGatewayProvider() + "'" +
            ", paymentGatewayCredentials='" + getPaymentGatewayCredentials() + "'" +
            "}";
    }
}
