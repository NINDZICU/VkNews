package com.therishka.androidlab_2.network;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import rx.AsyncEmitter;

/**
 * @author Rishad Mustafaev
 */

@SuppressWarnings("WeakerAccess")
public class RxVkRequestListener extends VKRequest.VKRequestListener {

    private AsyncEmitter<VKResponse> rxEmitter;

    public RxVkRequestListener(AsyncEmitter<VKResponse> rxEmitter) {
        this.rxEmitter = rxEmitter;
    }

    @Override
    public void onComplete(VKResponse response) {
        super.onComplete(response);
        rxEmitter.onNext(response);
    }

    @Override
    public void onError(VKError error) {
        super.onError(error);
        rxEmitter.onError(new Throwable(error.errorMessage));
    }

    @Override
    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
        super.onProgress(progressType, bytesLoaded, bytesTotal);
    }
}
