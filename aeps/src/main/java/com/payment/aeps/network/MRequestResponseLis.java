package com.payment.aeps.network;

public interface MRequestResponseLis {
    void onSuccessRequest(String JSonResponse);

    void onFailRequest(String msg);
}