package com.onestopstudiox.ads.sdk.format;

import static com.onestopstudiox.ads.sdk.util.Constant.ADMOB;
import static com.onestopstudiox.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.onestopstudiox.ads.sdk.util.Constant.NONE;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.onestopstudiox.ads.sdk.util.Tools;

@RequiresApi(api = Build.VERSION_CODES.S_V2)
public class RewardedAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private com.google.android.gms.ads.rewarded.RewardedAd adMobRewardedAd;

        private int retryAttempt;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobRewardedId = "";
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

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

    }
}
