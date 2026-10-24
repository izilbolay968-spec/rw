```java
package com.szeell.rw;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.GradientDrawable;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE = 42;
    private final int bg = Color.rgb(4, 17, 43);
    private final int cyan = Color.rgb(0, 190, 255);
    private final int gold = Color.rgb(255, 213, 80);

    private LinearLayout root;
    private TextView result;
    private TextView status;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        buildUi();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 26, 20, 24);
        root.setBackgroundColor(bg);
        scroll.setFillViewport(true);
        scroll.addView(root);

        TextView brand = new TextView(this);
        brand.setText("SZEELL RW");
        brand.setTextColor(cyan);
        brand.setTextSize(30);
        brand.setTypeface(Typeface.DEFAULT_BOLD);
        brand.setGravity(Gravity.CENTER);
        root.addView(brand);

        TextView title = new TextView(this);
        title.setText("TAHU RANK MUSUH");
        title.setTextColor(Color.WHITE);
        title.setTextSize(23);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 6, 0, 4);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Pemindai screenshot • OCR di perangkat");
        subtitle.setTextColor(0xff9edfff);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, 22);
        root.addView(subtitle);

        Button pick = new Button(this);
        pick.setText("PILIH SCREENSHOT MLBB");
        pick.setTextColor(Color.WHITE);
        pick.setTextSize(16);
        pick.setAllCaps(false);
        pick.setBackground(round(cyan, 18));
        pick.setOnClickListener(v -> chooseImage());
        root.addView(pick, matchWrap(58));

        status = new TextView(this);
        status.setText("Pilih screenshot lobby atau hasil pertandingan untuk mulai membaca teks.");
        status.setTextColor(0xffd4e9ff);
        status.setTextSize(14);
        status.setPadding(2, 18, 2, 12);
        root.addView(status);

        root.addView(section("HASIL PEMINDAIAN"));

        result = new TextView(this);
        result.setText("Belum ada screenshot yang dipindai.");
        result.setTextColor(Color.WHITE);
        result.setTextSize(16);
        result.setLineSpacing(5, 1f);
        result.setPadding(16, 16, 16, 16);
        result.setBackground(round(0xff0a2c52, 14));
        root.addView(result, matchWrap(-2));

        TextView note = new TextView(this);
        note.setText(
            "\nCATATAN\n" +
            "Aplikasi hanya membaca teks yang terlihat pada screenshot. " +
            "Rank yang tidak terlihat tidak bisa dipastikan, dan hasil OCR " +
            "dapat keliru. Aplikasi ini tidak mengakses server MLBB atau " +
            "mengubah permainan."
        );
        note.setTextColor(0xffa9c5df);
        note.setTextSize(13);
        note.setPadding(2, 12, 2, 4);
        root.addView(note);

        setContentView(scroll);
    }

    private TextView section(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextColor(gold);
        t.setTextSize(16);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setPadding(2, 12, 2, 10);
        return t;
    }

    private LinearLayout.LayoutParams matchWrap(int height) {
        return new LinearLayout.LayoutParams(-1, height);
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        d.setStroke(2, cyan);
        return d;
    }

    private void chooseImage() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");

        try {
            startActivityForResult
