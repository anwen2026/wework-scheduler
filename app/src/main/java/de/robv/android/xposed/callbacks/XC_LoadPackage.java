package de.robv.android.xposed.callbacks;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_LoadPackage;

public abstract class XC_LoadPackage implements IXposedHookLoadPackage {
    public static class LoadPackageParam extends XC_LoadPackage.LoadPackageParam {}
}
