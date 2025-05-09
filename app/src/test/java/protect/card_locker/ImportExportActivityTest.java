package protect.card_locker;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class ImportExportActivityTest {
    private void registerIntentHandler(String handler) {
        // Add something that will 'handle' the given intent type
        PackageManager packageManager = RuntimeEnvironment.application.getPackageManager();

        ResolveInfo info = new ResolveInfo();
        info.isDefault = true;

        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = "does.not.matter";
        info.activityInfo = new ActivityInfo();
        info.activityInfo.applicationInfo = applicationInfo;
        info.activityInfo.name = "DoesNotMatter";
        info.activityInfo.exported = true;

        Intent intent = new Intent(handler);

        if (handler.equals(Intent.ACTION_GET_CONTENT)) {
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
        }

        shadowOf(packageManager).addResolveInfoForIntent(intent, info);
    }

    private void checkVisibility(Activity activity, int state, int divider, int title, int message, int button) {
        View dividerView = activity.findViewById(divider);
        View titleView = activity.findViewById(title);
        View messageView = activity.findViewById(message);
        View buttonView = activity.findViewById(button);

        assertEquals(state, dividerView.getVisibility());
        assertEquals(state, titleView.getVisibility());
        assertEquals(state, messageView.getVisibility());
        assertEquals(state, buttonView.getVisibility());
    }

    @Test
    public void testAllOptionsAvailable() {
        registerIntentHandler(Intent.ACTION_PICK);
        registerIntentHandler(Intent.ACTION_GET_CONTENT);

        Activity activity = Robolectric.setupActivity(ImportExportActivity.class);

        checkVisibility(activity, View.VISIBLE, R.id.dividerImportFilesystem,
                R.id.importOptionFilesystemTitle, R.id.importOptionFilesystemExplanation,
                R.id.importOptionFilesystemButton);
    }
}
