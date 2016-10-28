package com.frame.aion.framedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aion.axframe.http.BaseResponse;
import com.aion.axframe.http.BaseSubscriber;
import com.aion.axframe.http.RetrofitClient;
import com.aion.axframe.utils.encrypt.MD5;
import com.aion.axframe.utils.log.ALog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    int mDuration = 500;
    float mCenterX = 0.0f;
    float mCenterY = 0.0f;
    float mDepthZ = 0.0f;
    @BindView(R.id.rotate)
    ImageView rotate;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.edit_depthz)
    EditText editDepthz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final int width = this.getResources().getDisplayMetrics().widthPixels;
        final int height = this.getResources().getDisplayMetrics().heightPixels;
        HashMap<String, String> params = new HashMap<>();
        params.put("offset", "0");
        params.put("limit", "1");
        params.put("_api_time", System.currentTimeMillis() / 1000 + "");
        params.put("_api_key", "i4TSNuSOuVqfnjgIPhbM44AbRL7ivofR");
        params.put("_api_format", "json");
        params.put("_api_token", "udxzxAqlrhGXQV-NZgb7-mpjD9kDPlac3e7bHpL2OTA");
        params.put("user_id", "9700685");
        params.put("_api_device", "2");
        params.put("_api_app_version", "2000");
        params.put("_api_app_type", "5");
        params = parserMap(params, "v1/user.getMyFollowerAllIds");
        ALog.d();
        RetrofitClient.getInstance().baseGet("v1/user.getMyFollowerAllIds", params, new BaseSubscriber<BaseResponse>() {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ALog.e(e);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseResponse response) {
                super.onNext(response);
                ALog.json(ALog.D, true, new Gson().toJson(response));
            }
        });
    }

    public HashMap parserMap(HashMap<String, String> map, String url) {
        HashMap<String, String> params = MD5.sortMapByValues(map);
        StringBuilder sb = new StringBuilder("/").append(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.replace(sb.length() - 1, sb.length(), "NwyzaZg4qTLx3b3mlwuy6nDp1JSzIKH2");
        String sign = MD5.GetMD5Code(sb.toString());
        params.put("_api_sign", sign);
        return params;
    }
}
