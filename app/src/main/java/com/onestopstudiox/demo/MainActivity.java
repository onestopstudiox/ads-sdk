package com.onestopstudiox.demo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.onestopstudiox.ads.sdk.format.AdNetwork;
import com.onestopstudiox.ads.sdk.format.BannerAd;
import com.onestopstudiox.ads.sdk.format.InterstitialAd;
import com.onestopstudiox.ads.sdk.format.NativeAd;
import com.onestopstudiox.ads.sdk.format.RewardedAd;

@RequiresApi(api = Build.VERSION_CODES.S_V2)
public class MainActivity extends AppCompatActivity {

    public static final String AD_STATUS = "1";
    public static final String AD_NETWORK = "admob";
    public static final String BACKUP_AD_NETWORK = "admob";

    public static final String ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String ADMOB_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    public static final String ADMOB_APP_OPEN_AD_ID = "ca-app-pub-3940256099942544/3419835294";

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
                .setDebug(false)
                .build();

        bannerAd = new BannerAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobBannerId(ADMOB_BANNER_ID)
                .build();

        interstitialAd = new InterstitialAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
                .setAdMobInterstitialId(ADMOB_INTERSTITIAL_ID)
                .setInterval(0)
                .build();

        rewardedAd = new RewardedAd.Builder(this)
                .setAdStatus(AD_STATUS)
                .setAdNetwork(AD_NETWORK)
                .setBackupAdNetwork(BACKUP_AD_NETWORK)
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