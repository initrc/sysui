package io.github.initrc.sysui;

import android.view.View;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Hide the IME Switcher from the bottom nav bar.
 */
public class HideImeSwitcher implements IXposedHookLoadPackage {

    private static final String SYSUI_PACKAGE = "com.android.systemui";
    private static final String NAV_BAR_VIEW_CLASS
            = "com.android.systemui.statusbar.phone.NavigationBarView";
    private View _imeSwitcherView;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(SYSUI_PACKAGE)) {
            return;
        }
        XposedHelpers.findAndHookMethod(NAV_BAR_VIEW_CLASS, lpparam.classLoader,
                "getImeSwitchButton", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                _imeSwitcherView = (View) param.getResult();
            }
        });
        XposedHelpers.findAndHookMethod(NAV_BAR_VIEW_CLASS, lpparam.classLoader,
                "setNavigationIconHints", int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (_imeSwitcherView != null) {
                    _imeSwitcherView.setVisibility(View.GONE);
                }
            }
        });
    }
}
