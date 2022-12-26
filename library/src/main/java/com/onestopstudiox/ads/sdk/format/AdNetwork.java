package com.onestopstudiox.ads.sdk.format;

import android.app.Activity;
import android.util.Log;

import static com.onestopstudiox.ads.sdk.util.Constant.ADMOB;
import static com.onestopstudiox.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.onestopstudiox.ads.sdk.util.Constant.APPLOVIN;
import static com.onestopstudiox.ads.sdk.util.Constant.NONE;
import static com.onestopstudiox.ads.sdk.util.Constant.STARTAPP;
import static com.onestopstudiox.ads.sdk.util.Constant.UNITY;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.onestopstudiox.ads.sdk.helper.AudienceNetworkInitializeHelper;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

import java.util.Map;

public class AdNetwork {

    public static class Initialize {

        private static final String TAG = "AdNetwork";
        Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String startappAppId = "0";
        private String unityGameId = "";
        private String appLovinSdkKey = "";
        private String mopubBannerId = "";
        private boolean debug = true;

        public Initialize(Activity activity) {
            this.activity = activity;
        }

        public Initialize build() {
            initAds();
            initBackupAds();
            return this;
        }

        public Initialize setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Initialize setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Initialize setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }


        public Initialize setStartappAppId(String startappAppId) {
            this.startappAppId = startappAppId;
            return this;
        }

        public Initialize setUnityGameId(String unityGameId) {
            this.unityGameId = unityGameId;
            return this;
        }

        public Initialize setAppLovinSdkKey(String appLovinSdkKey) {
            this.appLovinSdkKey = appLovinSdkKey;
            return this;
        }

        public Initialize setMopubBannerId(String mopubBannerId) {
            this.mopubBannerId = mopubBannerId;
            return this;
        }

        public Initialize setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public void initAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case ADMOB:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                                Log.d(TAG, String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, adapterStatus.getDescription(), adapterStatus.getLatency()));
                            }
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case UNITY:
                        UnityAds.initialize(activity.getApplicationContext(), unityGameId, debug, new IUnityAdsInitializationListener() {
                            @Override
                            public void onInitializationComplete() {
                                Log.d(TAG, "Unity Ads Initialization Complete with ID : " + unityGameId);
                            }

                            @Override
                            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                                Log.d(TAG, "Unity Ads Initialization Failed: [" + error + "] " + message);
                            }
                        });
                        break;
                    case APPLOVIN:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        final String sdkKey = AppLovinSdk.getInstance(activity).getSdkKey();
                        if (!sdkKey.equals(appLovinSdkKey)) {
                            Log.e(TAG, "ERROR : Please update your applovin sdk key in the manifest file.");
                        }
                        break;

                }
                Log.d(TAG, "[" + adNetwork + "] is selected as Primary Ads");
            }
        }

        public void initBackupAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case ADMOB:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                                Log.d(TAG, String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, adapterStatus.getDescription(), adapterStatus.getLatency()));
                            }
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case UNITY:
                        UnityAds.initialize(activity.getApplicationContext(), unityGameId, debug, new IUnityAdsInitializationListener() {
                            @Override
                            public void onInitializationComplete() {
                                Log.d(TAG, "Unity Ads Initialization Complete with ID : " + unityGameId);
                            }

                            @Override
                            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                                Log.d(TAG, "Unity Ads Initialization Failed: [" + error + "] " + message);
                            }
                        });
                        break;
                    case APPLOVIN:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        final String sdkKey = AppLovinSdk.getInstance(activity).getSdkKey();
                        if (!sdkKey.equals(appLovinSdkKey)) {
                            Log.e(TAG, "ERROR : Please update your applovin sdk key in the manifest file.");
                        }
                        break;


                    case NONE:
                        //do nothing
                        break;
                }
                Log.d(TAG, "[" + backupAdNetwork + "] is selected as Backup Ads");
            }
        }

    }
}
