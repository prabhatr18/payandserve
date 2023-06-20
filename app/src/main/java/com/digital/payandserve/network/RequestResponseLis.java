package com.digital.payandserve.network;

public interface RequestResponseLis {
    void onSuccessRequest(String JSonResponse);

    void onFailRequest(String msg);
}