package com.onestopstudiox.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.onestopstudiox.ads.sdk.format.AdNetwork;
import com.onestopstudiox.ads.sdk.format.BannerAd;
import com.onestopstudiox.ads.sdk.format.InterstitialAd;
import com.onestopstudiox.ads.sdk.format.NativeAd;
import com.onestopstudiox.ads.sdk.format.RewardedAd;

public class MainActivity extends AppCompatActivity {

    public static final String AD_STATUS = "1";
    public static final String AD_NETWORK = "admob";
    public static final String BACKUP_AD_NETWORK = "admob";

    public static final String ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String ADMOB_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    public static final String ADMOB_APP_OPEN_AD_ID = "ca-app-pub-3940256099942544/3419835294";

    public static final String STARTAPP_APP_ID = "0";

    public static final String UNITY_GAME_ID = "4856585";
    public static final String UNITY_BANNER_ID = "Banner_Android";
    public static final String UNITY_INTERSTITIAL_ID = "Interstitial_Android";
    public static final String UNITY_REWARDED_ID = "Rewarded_Android";

    public static final String APPLOVIN_BANNER_ID = "da17eff31ae69f15";
    public static final String APPLOVIN_INTERSTITIAL_ID = "98f6a586ed642919";
    public static final String APPLOVIN_NATIVE_ID = "f3f42da984bf7464";

    public static final String MOPUB_BANNER_ID = "b195f8dd8ded45fe847ad89ed1d016da";
    public static final String MOPUB_INTERSTITIAL_ID = "24534e1901884e398f1253216226017e";

    Toolbar toolbar;
    AdNetwork.Initialize adNetwork;
    BannerAd.Builder bannerAd;
    InterstitialAd.Builder interstitialAd;
    RewardedAd.Builder rewardedAd;
    NativeAd.Builder nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setStartappAppId(STARTAPP_APP_ID)
                .setUnityGameId(UNITY_GAME_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setMopubBannerId(MOPUB_BANNER_ID)
                .setDebug(false)
                .build();

        bannerAd = new BannerAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobBannerId(ADMOB_BANNER_ID)
                .setUnityBannerId(UNITY_BANNER_ID)
                .setAppLovinBannerId(APPLOVIN_BANNER_ID)
                .setDarkTheme(false)
                .build();

        interstitialAd = new InterstitialAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobInterstitialId(ADMOB_INTERSTITIAL_ID)
                .setUnityInterstitialId(UNITY_INTERSTITIAL_ID)
                .setAppLovinInterstitialId(APPLOVIN_INTERSTITIAL_ID)
                .setMopubInterstitialId(MOPUB_INTERSTITIAL_ID)
                .setInterval(0)
                .build();

        rewardedAd = new RewardedAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobRewardedId(ADMOB_REWARDED_ID)
                .setUnityRewardedId(UNITY_REWARDED_ID)
                .setInterval(0)
                .build();

        findViewById(R.id.btn_interstitial).setOnClickListener(v -> interstitialAd.show());
        findViewById(R.id.btn_rewarded).setOnClickListener(view -> rewardedAd.show());

        nativeAd = new NativeAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobNativeId(ADMOB_NATIVE_ID)
                .setDarkTheme(false)
                .build();



    }
}