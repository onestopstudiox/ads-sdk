package com.onestopstudiox.ads.sdk.format;

import static com.onestopstudiox.ads.sdk.util.Constant.ADMOB;
import static com.onestopstudiox.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.onestopstudiox.ads.sdk.util.Constant.APPLOVIN;
import static com.onestopstudiox.ads.sdk.util.Constant.NONE;
import static com.onestopstudiox.ads.sdk.util.Constant.STARTAPP;
import static com.onestopstudiox.ads.sdk.util.Constant.UNITY;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.onestopstudiox.ads.sdk.util.Tools;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class RewardedAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private com.google.android.gms.ads.rewarded.RewardedAd adMobRewardedAd;
        private StartAppAd startAppAd;

        private int retryAttempt;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobRewardedId = "";
        private String unityRewardedId = "";
        private int placementStatus = 1;
        private int interval = 0;

        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadRewardedAd();
            return this;
        }

        public void show() {
            showRewardedAd();
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

        public Builder setAdMobRewardedId(String adMobRewardedId) {
            this.adMobRewardedId = adMobRewardedId;
            return this;
        }

        public Builder setUnityRewardedId(String unityRewardedId) {
            this.unityRewardedId = unityRewardedId;
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

        public void loadRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd RewardedAd) {
                                adMobRewardedAd = RewardedAd;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadRewardedAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobRewardedAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "Admob Rewarded Ad onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobRewardedAd = null;
                                loadBackupRewardedAd();
                                Log.d(TAG, "Failed load AdMob Rewarded Ad");
                            }
                        });
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Rewarded Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Rewarded Ad");
                                loadBackupRewardedAd();
                            }
                        });
                        break;

                    case UNITY:
                        UnityAds.load(unityRewardedId, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                Log.d(TAG, "unity Rewarded ad loaded");
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Log.e(TAG, "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                                loadBackupRewardedAd();
                            }
                        });
                        break;

                    case APPLOVIN:

                        break;


                }
            }
        }

        public void loadBackupRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd RewardedAd) {
                                adMobRewardedAd = RewardedAd;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        loadRewardedAd();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        adMobRewardedAd = null;
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                                Log.i(TAG, "Admob Rewarded Ad onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                adMobRewardedAd = null;
                                Log.d(TAG, "Failed load AdMob Rewarded Ad");
                            }
                        });
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Rewarded Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Rewarded Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;

                    case UNITY:
                        UnityAds.load(unityRewardedId, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                Log.d(TAG, "unity Rewarded ad loaded");
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                Log.e(TAG, "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                            }
                        });
                        break;

                    case APPLOVIN:

                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                    switch (adNetwork) {
                        case ADMOB:
                            if (adMobRewardedAd != null) {
                                adMobRewardedAd.show(activity,new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        // Handle the reward.
                                        Log.d(TAG, "The user earned the reward.");
                                        int rewardAmount = rewardItem.getAmount();
                                        String rewardType = rewardItem.getType();
                                    }
                                });
                                Log.d(TAG, "admob Rewarded not null");
                            } else {
                                showBackupRewardedAd();
                                Log.d(TAG, "admob Rewarded null");
                            }
                            break;

                        case STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd();
                                Log.d(TAG, "startapp Rewarded not null  " );
                            } else {
                                showBackupRewardedAd();
                                Log.d(TAG, "startapp Rewarded null");
                            }
                            break;

                        case UNITY:
                            UnityAds.show(activity, unityRewardedId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                                @Override
                                public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                    Log.d(TAG, "unity ads show failure");
                                    showBackupRewardedAd();
                                }

                                @Override
                                public void onUnityAdsShowStart(String placementId) {

                                }

                                @Override
                                public void onUnityAdsShowClick(String placementId) {

                                }

                                @Override
                                public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                    loadRewardedAd();
                                }
                            });
                            break;

                        case APPLOVIN:
                            break;
                    }
            }
        }

        public void showBackupRewardedAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Rewarded Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case ADMOB:
                        if (adMobRewardedAd != null) {
                            adMobRewardedAd.show(activity,new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    Log.d(TAG, "The user earned the reward.");
                                    int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();
                                }
                            });
                        } else {
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                        }
                        break;

                    case UNITY:
                        UnityAds.show(activity, unityRewardedId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                Log.d(TAG, "unity ads show failure");
                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                loadBackupRewardedAd();
                            }
                        });
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

    }
}
