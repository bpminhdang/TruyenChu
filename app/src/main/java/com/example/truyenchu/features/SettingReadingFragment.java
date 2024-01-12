package com.example.truyenchu.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    private void applySavedTextColor(TextView mauchu) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int textColor = preferences.getInt("text_color", Color.BLACK);
        mauchu.setTextColor(textColor);
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

    // Hàm để lưu kích thước dòng vào SharedPreferences
    private void saveLineSpacing(float lineSpacing) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("line_spacing", lineSpacing);
        editor.apply();
    }

    // Hàm để lấy giá trị kích thước dòng từ SharedPreferences
    private float getSavedLineSpacing() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return preferences.getFloat("line_spacing", 0); // Giá trị mặc định nếu không có giá trị đã lưu
    }

    // Hàm để áp dụng kích thước dòng cho TextView
    private void applyLineSpacing(TextView textView, float lineSpacing) {
        // Áp dụng giá trị kích thước dòng cho TextView
        textView.setLineSpacing(lineSpacing, 1f);
    }

    // Lấy giá trị kích thước dòng từ SharedPreferences
    float lineSpacing = getSavedLineSpacing();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_reading, container, false);

        //nut back
        View back = view.findViewById(R.id.setting_back);
        back.setOnClickListener(v -> requireActivity().onBackPressed());

        // doi mau nen
        View cwhite = view.findViewById(R.id.col_white);
        View cblack = view.findViewById(R.id.col_black);
        View clgrey = view.findViewById(R.id.col_lightgrey);
        View cwbeige = view.findViewById(R.id.col_warmbeige);
        View cowhite = view.findViewById(R.id.col_offwhite);

        cwhite.setOnClickListener(v -> {
            saveBackgroundColor(Color.WHITE);
            view.setBackgroundColor(Color.WHITE);
        });

        cblack.setOnClickListener(v -> {
            saveBackgroundColor(Color.BLACK);
            view.setBackgroundColor(Color.BLACK);

            if (colorChangedListener != null) {
                colorChangedListener.onColorChanged(Color.BLACK);
            }
            // StoryReadingFragment storyReadingFragment2 = new StoryReadingFragment();
            int newColor = getSavedBackgroundColor();
            //  storyReadingFragment2.onColorChanged(newColor);

            // Thực hiện chuyển Fragment
            // FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // transaction.replace(R.id.reading_hihi, storyReadingFragment2);
            //  transaction.addToBackStack(null);
            //  transaction.commit();
        });

        clgrey.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#E2E2E2"));
            view.setBackgroundColor(Color.parseColor("#E2E2E2"));
        });

        cwbeige.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#f3dcba"));
            view.setBackgroundColor(Color.parseColor("#f3dcba"));
        });

        cowhite.setOnClickListener(v -> {
            saveBackgroundColor(Color.parseColor("#fff9ef"));
            view.setBackgroundColor(Color.parseColor("#fff9ef"));
        });
        int backgroundColor = getSavedBackgroundColor();
        //view.setBackgroundColor(backgroundColor);


        //doi mau chu
        View ctwhite = view.findViewById(R.id.coltx_white);
        View ctblack = view.findViewById(R.id.coltx_black);
        View ctlgrey = view.findViewById(R.id.coltx_lightgrey);
        View ctwbeige = view.findViewById(R.id.coltx_warmbeige);
        View ctlblue = view.findViewById(R.id.coltx_lblue);
        TextView mauchu = view.findViewById(R.id.tx_mauchu);

        //applySavedTextColor(mauchu);

        ctwhite.setOnClickListener(v -> {
            int newColor = Color.WHITE;
            mauchu.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctblack.setOnClickListener(v -> {
            int newColor = Color.BLACK;
            mauchu.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctlgrey.setOnClickListener(v -> {
            int newColor = Color.parseColor("#E2E2E2");
            mauchu.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctwbeige.setOnClickListener(v -> {
            int newColor = Color.parseColor("#f3dcba");
            mauchu.setTextColor(newColor);
            saveTextColor(newColor);
        });

        ctlblue.setOnClickListener(v -> {
            int newColor = Color.parseColor("#DCF2F1");
            mauchu.setTextColor(newColor);
            saveTextColor(newColor);
        });


        //tang giam size text
        Button increase = view.findViewById(R.id.cus_tang);
        Button decrease = view.findViewById(R.id.cus_giam);

        float textSize = getSavedTextSize();
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tăng kích thước văn bản
                ourFontsize += 3f; //chinh gia tri mac dinh o line 121 file nay
                saveTextSize(ourFontsize);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Giam kích thước văn bản
                ourFontsize -= 3f;
                saveTextSize(ourFontsize);
            }
        });
        Toast.makeText(getActivity(), String.valueOf(ourFontsize), Toast.LENGTH_SHORT).show();

        //tang giam size dong`
        View inline = view.findViewById(R.id.cus_tangln);
        View deline = view.findViewById(R.id.cus_giamln);
        applyLineSpacing(mauchu, lineSpacing);


        // Áp dụng giá trị kích thước dòng cho TextView
        inline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng kích thước dòng
                lineSpacing += 1f;
                // Lưu giá trị mới vào SharedPreferences
                saveLineSpacing(lineSpacing);
                // Áp dụng giá trị mới cho TextView
                applyLineSpacing(mauchu, lineSpacing);
            }
        });

        deline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giảm kích thước dòng
                lineSpacing -= 1f;
                // Lưu giá trị mới vào SharedPreferences
                saveLineSpacing(lineSpacing);
                // Áp dụng giá trị mới cho TextView
                applyLineSpacing(mauchu, lineSpacing);
            }
        });
 //nãy giờ làm trên 2 cái fragment reading thôi, nên giờ t pull thì có mất ko
        return view;
    }

}