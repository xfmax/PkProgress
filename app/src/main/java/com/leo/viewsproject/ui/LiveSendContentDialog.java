package com.leo.viewsproject.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.leo.viewsproject.R;
import com.leo.viewsproject.widget.RecordVoiceView;
import com.leo.viewsproject.widget.indicator.CustomTitleAdapter;
import com.leo.viewsproject.widget.indicator.TitleContainer;

/**
 * Describe : 发送文本弹窗
 * 直播房间发送文本消息、喇叭消息
 * Created by Leo on 2019/4/15.
 */
public class LiveSendContentDialog extends Dialog {

    private static final String TAG = "LiveSendContentDialog";
    private Context mContext;
    private SoftKeyboardListenEditText mEvText;
    private SendTextListener listener;
    private TitleContainer titleContainer;
    private FrameLayout flContainer;
    private RecordVoiceView recordVoiceView;
    private TextView tvRecordState;

    //   private ToggleButton mToggleButton;
//   private Button mSubmitButton;
    private int mExpandMaxY;
    private int mExpandMinY;

    private boolean chatFlag = true;
    private String mTargetName;        // @用户昵称
    private int mTargetUid;            // @用户id

    private int mDialogTag;            // 发送文本类型  0 普通文本、 1 喇叭文本

    /**
     * 发送文本消息 设置@功能
     *
     * @param context  context
     * @param name     需@的用户昵称
     * @param uid      需@的用户id
     * @param listener 发送回调
     */
    public LiveSendContentDialog(@NonNull Context context, String name, int uid, SendTextListener listener) {
        super(context, R.style.SendTextDialog);
        mContext = context;
        this.listener = listener;
        this.mTargetName = name;
        this.mTargetUid = uid;
    }

    public LiveSendContentDialog(@NonNull Context context, SendTextListener listener) {
        super(context, R.style.SendTextDialog);
        mContext = context;
        this.listener = listener;
    }

    /**
     * 根据类型区分 发送文本消息/喇叭消息
     *
     * @param context   context
     * @param dialogTag 0  聊天   1 喇叭
     * @param listener  发送回调
     */
    public LiveSendContentDialog(@NonNull Context context, int dialogTag, SendTextListener listener) {
        super(context, R.style.SendTextDialog);
        mContext = context;
        this.listener = listener;
        this.mDialogTag = dialogTag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        getWindow().getAttributes().width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;

        initView();

        CustomTitleAdapter customTitleAdapter = new CustomTitleAdapter();
        titleContainer.setTitleView(customTitleAdapter);
        customTitleAdapter.setTabClick(new CustomTitleAdapter.TabClick() {
            @Override
            public void onSelected(int position, int totalCount) {
                setFlContainer(position, 0);
                titleContainer.onSelected(position, -1, totalCount);
            }
        });

        setListener();
    }

    private void initView() {
        flContainer = findViewById(R.id.fl_container);
        mEvText = findViewById(R.id.chat_txt_editTxt);
        titleContainer = findViewById(R.id.indicatorView);
        recordVoiceView = findViewById(R.id.view_record);
        tvRecordState = findViewById(R.id.tv_record_state);
    }

    private void setListener() {

        mEvText.setListener(new SoftKeyboardListenEditText.OnSoftKeyboardDownListener() {
            @Override
            public void hideSoft() {
                if (isShowing()) {
                    dismiss();
                }
            }

            @Override
            public void showSoft() {
            }
        });

        findViewById(R.id.root).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mEvText != null) {

                    int[] l = new int[2];
                    mEvText.getLocationOnScreen(l);
                    if (l[1] >= mExpandMaxY) {
                        mExpandMaxY = l[1];
                    } else {
                        mExpandMinY = l[1];
                    }

                    if (mExpandMinY != 0 && mExpandMaxY != 0) {
                        if (l[1] > mExpandMinY && isShowing()) {
//                            dismiss();
                        }
                    }
                }
            }
        });

        mEvText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND) {
                    String sendText = mEvText.getText().toString().trim();

                    boolean result = listener.send(mEvText, sendText, chatFlag, mTargetName, mTargetUid);

                    clearInput(true);
                    if (!result) {
                        dismiss();
                    }
                    return result;
                }
                return false;
            }
        });

        recordVoiceView.setRecordState(new RecordVoiceView.IRecordState() {

            @Override
            public void onStart() {
                tvRecordState.setText("按下录制");
                Log.e(TAG, "开始");
            }

            @Override
            public void onStartRecord() {
                tvRecordState.setText("按下录制");
                Log.e(TAG, "开始");
            }

            @Override
            public void onRecord() {
                tvRecordState.setText("左滑取消，右滑发送");
                Log.e(TAG, "录制中");
            }

            @Override
            public void onSend() {
                tvRecordState.setText("发送完成");
                Log.e(TAG, "发送");
            }

            @Override
            public void onCancel() {
                tvRecordState.setText("销毁");
                tvRecordState.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvRecordState.setText("按下录制");
                    }
                }, 2000);
                Log.e(TAG, "取消");
            }
        });
    }

    @Override
    public void dismiss() {
        hideSoftInput();
        flContainer.setVisibility(View.GONE);
        titleContainer.onSelected(0, -1, 5);
        super.dismiss();
        mExpandMaxY = mExpandMinY = 0;
    }

    private void hideSoftInput() {
        mEvText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive() && getWindow() != null && getWindow().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                    mEvText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showSoftInput() {
        mEvText.setVisibility(View.VISIBLE);
        mEvText.requestFocus();
        mEvText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive() && getWindow() != null && getWindow().getCurrentFocus() != null) {
                    imm.showSoftInput(mEvText, 0);
                }
            }
        });
    }

    private int mkeyBoardHeight;

    public void setFlContainer(int index, int height) {
        if (height > 0 && mkeyBoardHeight == 0) {
            mkeyBoardHeight = height;
        }

        if (index < 0) {
            return;
        }

        if (index != 4) {
            showSoftInput();
            flContainer.setVisibility(View.GONE);
            return;
        }

        if (flContainer.getVisibility() == View.VISIBLE) {
            return;
        }

        hideSoftInput();
        Log.e("Leo height", mkeyBoardHeight + "");
//        flContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mkeyBoardHeight));

        flContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                flContainer.setVisibility(View.VISIBLE);
            }
        }, 50);
    }

    public void clearInput(boolean clear) {
        if (clear) {
            mEvText.setText("");
        }
    }

    public interface SendTextListener {

        /**
         * 返回值决定是否收起dialog以及软键盘，
         * true：不收起，不消失
         * false 收起软键盘，dialog消失
         *
         * @param view       输入框控件
         * @param text       输入框文本
         * @param chatFlag   公私聊消息/大小喇叭
         * @param targetName @目标用户昵称
         * @param targetUid  @目标用户uid
         * @return 是否收起dialog以及软键盘
         */
        boolean send(EditText view, String text, boolean chatFlag, String targetName, int targetUid);
    }
}
