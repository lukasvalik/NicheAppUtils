package eu.valics.library.View;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import eu.valics.library.R;
import eu.valics.library.Utils.permissionmanagement.BasePermission;

/**
 * Created by lukas on 01.11.2017.
 */

public class SettingsPermissionDialog extends AlertDialog {

    private int style;
    private AppCompatButton positiveButton;

    private SettingsPermissionDialog(Context context,
                                       CharSequence title,
                                       CharSequence message,
                                       Drawable image,
                                       Drawable buttonBackground) {
        super(context);
        initViews(context, title, message, image, buttonBackground);
    }

    private SettingsPermissionDialog(Context context,
                                     int style,
                                     CharSequence title,
                                     CharSequence message,
                                     Drawable image,
                                     Drawable buttonBackground) {
        super(context, style);
        initViews(context, title, message, image, buttonBackground);
    }

    private void initViews(Context context, CharSequence title, CharSequence message, Drawable image, Drawable buttonBackground) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_permission_settings, null);
        ((AppCompatTextView)view.findViewById(R.id.title)).setText(title);
        ((AppCompatTextView)view.findViewById(R.id.message)).setText(message);
        ((AppCompatImageView)view.findViewById(R.id.image)).setBackgroundDrawable(image);

        positiveButton = view.findViewById(R.id.positive_button);
        positiveButton.setBackground(buttonBackground);

        setView(view);
    }

    public void setPositiveButtonClickListener(View.OnClickListener onClickListener) {
        positiveButton.setOnClickListener(onClickListener);
    }


    public static class Builder {

        private Context context;
        private CharSequence title;
        private CharSequence message;
        private Drawable image;
        private Drawable buttonBackground;
        private View.OnClickListener positiveClickListener;
        private int style = BasePermission.DEFAULT_STYLE;

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setImageDescription(Drawable imageDescription) {
            this.image = imageDescription;
            return this;
        }

        public Builder setButtonBackground(Drawable drawable) {
            this.buttonBackground = drawable;
            return this;
        }

        public Builder style(int style) {
            this.style = style;
            return this;
        }

        public SettingsPermissionDialog build() {
            return style == BasePermission.DEFAULT_STYLE ?
                    new SettingsPermissionDialog(context, title, message, image, buttonBackground) :
                    new SettingsPermissionDialog(context, style, title, message, image, buttonBackground);
        }
    }
}
