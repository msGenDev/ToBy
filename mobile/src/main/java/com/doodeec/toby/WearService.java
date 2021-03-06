package com.doodeec.toby;

import android.util.Log;

import com.doodeec.toby.appstate.AppData;
import com.doodeec.tobycommon.sync.DataSync;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * @author dusan.bartos
 */
public class WearService extends WearableListenerService {

    private static final String TAG = "TOBY_SERVICE";

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "WEAR create");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.d(TAG, "WEAR Data changed " + dataEvents);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "WEAR Message " + messageEvent);
        if (messageEvent.getPath().equals(DataSync.SYNC_REQUEST_PATH)) {
            sendData();
        }
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
        Log.d(TAG, "WEAR Connected " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);
        Log.d(TAG, "WEAR Disconnected " + peer);
    }

    public void sendData() {
        String lists = AppData.getInstance().getShoppingListsAsJSON().toString();
        Asset shoppingListsAsset = Asset.createFromBytes(lists.getBytes());

        PutDataMapRequest dataMap = PutDataMapRequest.create(DataSync.SYNC_LIST_DATA);
        dataMap.getDataMap().putAsset(DataSync.SYNC_SHOPPING_LIST, shoppingListsAsset);
        PutDataRequest request = dataMap.asPutDataRequest();
        Log.d("TOBY", "Send lists to wearable " + lists);
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d("TOBY", "Sending shopping lists successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });
    }
}
