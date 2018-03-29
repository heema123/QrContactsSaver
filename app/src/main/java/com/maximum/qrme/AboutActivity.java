package com.maximum.qrme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class AboutActivity extends MaterialAboutActivity {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {

        MaterialAboutCard.Builder card = new MaterialAboutCard.Builder();
                // Configure card here
                card.addItem(new MaterialAboutTitleItem.Builder()
                        .text("QR Contact Saver")
                        .desc("© 2017 Ibrahim Doso")
                        .icon(R.mipmap.ic_launcher)
                        .build());
                card.addItem(new MaterialAboutActionItem.Builder()
                        .text("Version")
                        .subText("1.0")
                        .icon(R.drawable.ic_info_outline_black_24dp)
                        .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Ibrahim Adel Ibrahim")
                .subText("Egypt")
                .icon(R.drawable.ic_person_black_24dp)
        .build());
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("https://github.com/heema123")
                .icon(R.drawable.github512)
                .build()
        );

        MaterialAboutCard.Builder thanksCard = new MaterialAboutCard.Builder();
        thanksCard.title("Credits");
        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Special Thanks to")
                .icon(R.drawable.thanks512)
                .build());

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("blikoon/QRCodeScanner")
                        .subText("https://github.com/blikoon/QRCodeScanner")
                        .icon(R.drawable.github512)
        .build()
        );

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("bumptech/glide")
                .subText("https://github.com/bumptech/glide")
                .icon(R.drawable.github512)
                .build()
        );

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("anderscheow/Validator")
                .subText("https://github.com/anderscheow/Validator")
                .icon(R.drawable.github512)
                .build()
        );

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("81813780/AVLoadingIndicatorView")
                .subText("https://github.com/81813780/AVLoadingIndicatorView")
                .icon(R.drawable.github512)
                .build()
        );

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Kishanjvaghela/Ask-Permission")
                .subText("https://github.com/Kishanjvaghela/Ask-Permission")
                .icon(R.drawable.github512)
                .build()
        );

        thanksCard.addItem(new MaterialAboutActionItem.Builder()
                .text("daniel-stoneuk/material-about-library")
                .subText("https://github.com/daniel-stoneuk/material-about-library")
                .icon(R.drawable.github512)
                .build()
        );

        MaterialAboutCard.Builder rateAndShare = new MaterialAboutCard.Builder();

        rateAndShare.addItem(new MaterialAboutActionItem.Builder()
                .text("Leave Review")
                .icon(R.drawable.ic_star_yellow_900_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.maximum.qrme"));
                        startActivity(intent);
                    }
                })
                .build()
        );

        rateAndShare.addItem(new MaterialAboutActionItem.Builder()
                .text("Share")
                .icon(R.drawable.ic_share_blue_grey_700_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        onInviteClicked();
                    }
                })
                .build()
        );

        return new MaterialAboutList(card.build(), authorCardBuilder.build(), thanksCard.build(), rateAndShare.build());
    }

    private void onInviteClicked() {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=com.maximum.qrme";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "QR Contact Saver");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

}
