package com.urbanthreads.orderservice.DTO;

import java.util.List;

public class CustomerOrderDTO {
    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public PaymentDetailsDTO getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetailsDTO paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public ShippingInfoDTO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoDTO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    private List<ItemDTO> items;
    private PaymentDetailsDTO paymentDetails;
    private ShippingInfoDTO shippingInfo;

}
