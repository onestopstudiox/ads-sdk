package com.onestopstudiox.ads.sdk.format;

import static com.onestopstudiox.ads.sdk.util.Constant.ADMOB;
import static com.onestopstudiox.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.onestopstudiox.ads.sdk.util.Constant.NONE;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.onestopstudiox.ads.sdk.util.Tools;

public class InterstitialAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private com.google.android.gms.ads.interstitial.InterstitialAd adMobInterstitialAd;
        private int retryAttempt;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobInterstitialId = "";
        private int placementStatus = 1;
        private int interval = 0;

        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadInterstitialAd();
            return this;
        }

        public void show() {
            showInterstitialAd();
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobInterstitialId(String adMobInterstitialId) {
            this.adMobInterstitialId = adMobInterstitialId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "Admob Interstitial Ad onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                loadBackupInterstitialAd();
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                }
            }
        }

        public void loadBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, adMobInterstitialId, Tools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                adMobInterstitialAd = interstitialAd;
                                adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadInterstitialAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobInterstitialAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobInterstitialAd = null;
                                Log.d(TAG, "Failed load AdMob Interstitial Ad");
                            }
                        });
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                    switch (adNetwork) {
                        case ADMOB:
                            if (adMobInterstitialAd != null) {
                                adMobInterstitialAd.show(activity);
                                Log.d(TAG, "admob interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "admob interstitial null");
                            }
                            break;

                    }

            }
        }

        public void showBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case ADMOB:
                        if (adMobInterstitialAd != null) {
                            adMobInterstitialAd.show(activity);
                        }
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

    }
}
