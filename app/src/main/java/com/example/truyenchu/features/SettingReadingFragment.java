package com.example.truyenchu.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truyenchu.Login;
import com.example.truyenchu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingReadingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //cuu

    public interface OnColorChangedListener {
        void onColorChanged(int newColor);

        void setOnColorChangedListener(Object saveBackgroundColor);
    }

    private OnColorChangedListener colorChangedListener;

    //... rest of the code

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.colorChangedListener = listener;
    }


    //cuuuu
    public SettingReadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingReadingFragment.
     */
    public static SettingReadingFragment newInstance(String param1, String param2) {
        SettingReadingFragment fragment = new SettingReadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //lay mau bg da co san
    private int getSavedBackgroundColor() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getInt("background_color", getResources().getColor(android.R.color.white));
    }

    // luu mau bg
    private void saveBackgroundColor(int color) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("background_color", color);
        editor.apply();
    }

    //luu mau text
    private void saveTextColor(int color) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("text_color", color);
        editor.apply();
    }

    //apply mau text da co khi out ra vo lai
    private void applySavedTextColor(TextView testne) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int textColor = preferences.getInt("text_color", Color.BLACK);
        testne.setTextColor(textColor);
    }

    //lay textsize dang co
    private float getSavedTextSize() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getFloat("text_size", ourFontsize); //
    }

    //luu textsize vao
    private void saveTextSize(float size) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("text_size", size);
        editor.apply();
    }

    private float ourFontsize = 16f; //bien' size text

    // Hàm áp dụng giá trị kích thước dòng mới cho TextView
    private void applyLineSpacingMultiplier(TextView textView, float multiplier) {
        textView.setLineSpacing(0, multiplier);
    }

    // Hàm lưu giá trị kích thước dòng mới vào SharedPreferences
    private void saveLineSpacingMultiplier(float multiplier) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("line_spacing_multiplier", multiplier);
        editor.apply();
    }

    // Hàm lấy giá trị kích thước dòng từ SharedPreferences
    private float getSavedLineSpacingMultiplier() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getFloat("line_spacing_multiplier", 1.0f);
    }

    private float LineSpacingMultiplier = 1.0f; // Giá trị mặc định

    //doi font
    private static final String PREF_SELECTED_FONT_PATH = "selected_font_path";

    // Hàm đọc tên font đã chọn từ SharedPreferences
    private void saveSelectedFontPath(String fontPath) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_SELECTED_FONT_PATH, fontPath);
        editor.apply();
    }

    // Hàm lấy đường dẫn font đã chọn từ SharedPreferences
    private String getSelectedFontPath() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getString(PREF_SELECTED_FONT_PATH, "");
    }

    // Hàm áp dụng font từ đường dẫn đã chọn
    private void applySavedFont(TextView textView) {
        String fontPath = getSelectedFontPath();

        if (!fontPath.isEmpty()) {
            // Tạo Typeface từ đường dẫn đã lưu
            Typeface savedFont = Typeface.createFromAsset(requireActivity().getAssets(), fontPath);
            textView.setTypeface(savedFont);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_reading, container, false);

        //nut back
        View back = view.findViewById(R.id.setting_back);
        back.setOnClickListener(v -> requireActivity().onBackPressed());

        // doi mau nen
        View cday1= view.findViewById(R.id.col_day1);
        View cday2 = view.findViewById(R.id.col_day2);
        View cday3 = view.findViewById(R.id.col_day3);
        View cday4 = view.findViewById(R.id.col_day4);
        View cday5 = view.findViewById(R.id.col_day5);
        View cnight1= view.findViewById(R.id.col_night1);
        View cnight2 = view.findViewById(R.id.col_night2);
        View cnight3 = view.findViewById(R.id.col_night3);
        View cnight4 = view.findViewById(R.id.col_night4);
        View cnight5 = view.findViewById(R.id.col_night5);
        View testbg=view.findViewById(R.id.test);

        int backgroundColor = getSavedBackgroundColor();
        testbg.setBackgroundColor(backgroundColor);


        cday1.setOnClickListener(v -> {
            saveBackgroundColor(Color.WHITE);
            testbg.setBackgroundColor(Color.WHITE);
        });

        cday2.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#E2E2E2"));
            testbg.setBackgroundColor(Color.parseColor("#E2E2E2"));
        });

        cday3.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#fff9ef"));
            testbg.setBackgroundColor(Color.parseColor("#fff9ef"));
        });

        cday4.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#f3dcba"));
            testbg.setBackgroundColor(Color.parseColor("#f3dcba"));
        });

        cday5.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#FFFACD"));
            testbg.setBackgroundColor(Color.parseColor("#FFFACD"));
        });

        cnight1.setOnClickListener(v -> {
            saveBackgroundColor(Color.BLACK);
            testbg.setBackgroundColor(Color.BLACK);
        });

        cnight2.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#1C1C1C"));
            testbg.setBackgroundColor(Color.parseColor("#1C1C1C"));
        });

        cnight3.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#333333"));
            testbg.setBackgroundColor(Color.parseColor("#333333"));
        });

        cnight4.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#0E253A"));
            testbg.setBackgroundColor(Color.parseColor("#0E253A"));
        });

        cnight5.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#120037"));
            testbg.setBackgroundColor(Color.parseColor("#120037"));
        });


        //doi mau chu
        View ctday1= view.findViewById(R.id.coltx_day1);
        View ctday2 = view.findViewById(R.id.coltx_day2);
        View ctday3 = view.findViewById(R.id.coltx_day3);
        View ctday4 = view.findViewById(R.id.coltx_day4);
        View ctday5 = view.findViewById(R.id.coltx_day5);
        View ctnight1= view.findViewById(R.id.coltx_night1);
        View ctnight2 = view.findViewById(R.id.coltx_night2);
        View ctnight3 = view.findViewById(R.id.coltx_night3);
        View ctnight4 = view.findViewById(R.id.coltx_night4);
        View ctnight5 = view.findViewById(R.id.coltx_night5);
        TextView testne = view.findViewById(R.id.test_tx);

        applySavedTextColor(testne);

        ctday1.setOnClickListener(v -> {
            int newColor = Color.WHITE;
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctday2.setOnClickListener(v -> {
            int newColor = Color.parseColor("#F2F2F2");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctday3.setOnClickListener(v -> {
            int newColor = Color.parseColor("#C1CDCD");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctday4.setOnClickListener(v -> {
            int newColor = Color.parseColor("#FFFFCC");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctday5.setOnClickListener(v -> {
            int newColor = Color.parseColor("#DCF2F1");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });
        ctnight1.setOnClickListener(v -> {
            int newColor = Color.BLACK;
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctnight2.setOnClickListener(v -> {
            int newColor = Color.parseColor("#333333");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctnight3.setOnClickListener(v -> {
            int newColor = Color.parseColor("#8B4513");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctnight4.setOnClickListener(v -> {
            int newColor = Color.parseColor("#008000");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctnight5.setOnClickListener(v -> {
            int newColor = Color.parseColor("#000080");
            testne.setTextColor(newColor);
            saveTextColor(newColor);
        });

        //tang giam size text
        Button increase = view.findViewById(R.id.cus_tang);
        Button decrease = view.findViewById(R.id.cus_giam);

        float textSize = getSavedTextSize();
        testne.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tăng kích thước văn bản
                ourFontsize += 3f; //chinh gia tri mac dinh o line 121 file nay
                saveTextSize(ourFontsize);
                testne.setTextSize(TypedValue.COMPLEX_UNIT_SP, ourFontsize);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Giam kích thước văn bản
                ourFontsize -= 3f;
                saveTextSize(ourFontsize);
                testne.setTextSize(TypedValue.COMPLEX_UNIT_SP, ourFontsize);
            }
        });

        //tang giam size dong`
        View inline = view.findViewById(R.id.cus_tangln);
        View deline = view.findViewById(R.id.cus_giamln);
        float lineSpacingMultiplier = getSavedLineSpacingMultiplier();
        applyLineSpacingMultiplier(testne, lineSpacingMultiplier);

        // Áp dụng giá trị kích thước dòng cho TextView
        inline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng kích thước dòng
                LineSpacingMultiplier += 0.3f;

                // Lưu giá trị mới vào SharedPreferences
                saveLineSpacingMultiplier(LineSpacingMultiplier);

                applyLineSpacingMultiplier(testne, LineSpacingMultiplier);


            }
        });

        deline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm kích thước dòng
                LineSpacingMultiplier -= 0.1f;

                // Lưu giá trị mới vào SharedPreferences
                saveLineSpacingMultiplier(LineSpacingMultiplier);

                applyLineSpacingMultiplier(testne, LineSpacingMultiplier);

            }
        });

        //doi font
        View hel = view.findViewById(R.id.helveticaneue);
        View ari = view.findViewById(R.id.arial);
        View avir = view.findViewById(R.id.avenir);
        View time = view.findViewById(R.id.times);
        View geo = view.findViewById(R.id.georgia);
        View rob = view.findViewById(R.id.roboto);
        View cen = view.findViewById(R.id.centur);
        View van = view.findViewById(R.id.uvnvan);
        View mont = view.findViewById(R.id.monts);
        View lite = view.findViewById(R.id.literata);
        applySavedFont(testne);
        hel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "helveticaneue.ttf");
                saveSelectedFontPath("helveticaneue.ttf");
                applySavedFont(testne);
            }
        });
        ari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "arial.ttf");
                saveSelectedFontPath("arial.ttf");
                applySavedFont(testne);
            }
        });
        avir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "avenirnextltpro_regular.otf");
                saveSelectedFontPath("avenirnextltpro_regular.otf");
                applySavedFont(testne);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "timesnewroman.ttf");
                saveSelectedFontPath("timesnewroman.ttf");
                applySavedFont(testne);
            }
        });

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "georgia.ttf");
                saveSelectedFontPath("georgia.ttf");
                applySavedFont(testne);
            }
        });
        rob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "roboto_regular.ttf");
                saveSelectedFontPath("roboto_regular.ttf");
                applySavedFont(testne);
            }
        });
        cen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "utm_centur.ttf");
                saveSelectedFontPath("utm_centur.ttf");
                applySavedFont(testne);
            }
        });
        van.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "uvn_van.TTF");
                saveSelectedFontPath("uvn_van.TTF");
                applySavedFont(testne);
            }
        });
        mont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "montserrat_regular.otf");
                saveSelectedFontPath("montserrat_regular.otf");
                applySavedFont(testne);
            }
        });
        lite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(), "literatabook.otf");
                saveSelectedFontPath("literatabook.otf");
                applySavedFont(testne);
            }
        });
        return view;
    }

}