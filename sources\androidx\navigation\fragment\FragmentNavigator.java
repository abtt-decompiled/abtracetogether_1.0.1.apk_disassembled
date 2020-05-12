package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.Navigator.Name;
import androidx.navigation.NavigatorProvider;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Name("fragment")
public class FragmentNavigator extends Navigator<Destination> {
    private static final String KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds";
    private static final String TAG = "FragmentNavigator";
    private ArrayDeque<Integer> mBackStack = new ArrayDeque<>();
    private final int mContainerId;
    private final Context mContext;
    private final FragmentManager mFragmentManager;

    public static class Destination extends NavDestination {
        private String mClassName;

        public Destination(NavigatorProvider navigatorProvider) {
            this(navigatorProvider.getNavigator(FragmentNavigator.class));
        }

        public Destination(Navigator<? extends Destination> navigator) {
            super(navigator);
        }

        public void onInflate(Context context, AttributeSet attributeSet) {
            super.onInflate(context, attributeSet);
            TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, R.styleable.FragmentNavigator);
            String string = obtainAttributes.getString(R.styleable.FragmentNavigator_android_name);
            if (string != null) {
                setClassName(string);
            }
            obtainAttributes.recycle();
        }

        public final Destination setClassName(String str) {
            this.mClassName = str;
            return this;
        }

        public final String getClassName() {
            String str = this.mClassName;
            if (str != null) {
                return str;
            }
            throw new IllegalStateException("Fragment class was not set");
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" class=");
            String str = this.mClassName;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            return sb.toString();
        }
    }

    public static final class Extras implements androidx.navigation.Navigator.Extras {
        private final LinkedHashMap<View, String> mSharedElements;

        public static final class Builder {
            private final LinkedHashMap<View, String> mSharedElements = new LinkedHashMap<>();

            public Builder addSharedElements(Map<View, String> map) {
                for (Entry entry : map.entrySet()) {
                    View view = (View) entry.getKey();
                    String str = (String) entry.getValue();
                    if (!(view == null || str == null)) {
                        addSharedElement(view, str);
                    }
                }
                return this;
            }

            public Builder addSharedElement(View view, String str) {
                this.mSharedElements.put(view, str);
                return this;
            }

            public Extras build() {
                return new Extras(this.mSharedElements);
            }
        }

        Extras(Map<View, String> map) {
            LinkedHashMap<View, String> linkedHashMap = new LinkedHashMap<>();
            this.mSharedElements = linkedHashMap;
            linkedHashMap.putAll(map);
        }

        public Map<View, String> getSharedElements() {
            return Collections.unmodifiableMap(this.mSharedElements);
        }
    }

    public FragmentNavigator(Context context, FragmentManager fragmentManager, int i) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mContainerId = i;
    }

    public boolean popBackStack() {
        if (this.mBackStack.isEmpty()) {
            return false;
        }
        if (this.mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return false;
        }
        this.mFragmentManager.popBackStack(generateBackStackName(this.mBackStack.size(), ((Integer) this.mBackStack.peekLast()).intValue()), 1);
        this.mBackStack.removeLast();
        return true;
    }

    public Destination createDestination() {
        return new Destination((Navigator<? extends Destination>) this);
    }

    @Deprecated
    public Fragment instantiateFragment(Context context, FragmentManager fragmentManager, String str, Bundle bundle) {
        return fragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0134 A[RETURN] */
    public NavDestination navigate(Destination destination, Bundle bundle, NavOptions navOptions, androidx.navigation.Navigator.Extras extras) {
        if (this.mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state");
            return null;
        }
        String className = destination.getClassName();
        boolean z = false;
        if (className.charAt(0) == '.') {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mContext.getPackageName());
            sb.append(className);
            className = sb.toString();
        }
        Fragment instantiateFragment = instantiateFragment(this.mContext, this.mFragmentManager, className, bundle);
        instantiateFragment.setArguments(bundle);
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (!(enterAnim == -1 && exitAnim == -1 && popEnterAnim == -1 && popExitAnim == -1)) {
            if (enterAnim == -1) {
                enterAnim = 0;
            }
            if (exitAnim == -1) {
                exitAnim = 0;
            }
            if (popEnterAnim == -1) {
                popEnterAnim = 0;
            }
            if (popExitAnim == -1) {
                popExitAnim = 0;
            }
            beginTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }
        beginTransaction.replace(this.mContainerId, instantiateFragment);
        beginTransaction.setPrimaryNavigationFragment(instantiateFragment);
        int id = destination.getId();
        boolean isEmpty = this.mBackStack.isEmpty();
        boolean z2 = navOptions != null && !isEmpty && navOptions.shouldLaunchSingleTop() && ((Integer) this.mBackStack.peekLast()).intValue() == id;
        if (!isEmpty) {
            if (z2) {
                if (this.mBackStack.size() > 1) {
                    this.mFragmentManager.popBackStack(generateBackStackName(this.mBackStack.size(), ((Integer) this.mBackStack.peekLast()).intValue()), 1);
                    beginTransaction.addToBackStack(generateBackStackName(this.mBackStack.size(), id));
                }
                if (extras instanceof Extras) {
                    for (Entry entry : ((Extras) extras).getSharedElements().entrySet()) {
                        beginTransaction.addSharedElement((View) entry.getKey(), (String) entry.getValue());
                    }
                }
                beginTransaction.setReorderingAllowed(true);
                beginTransaction.commit();
                if (z) {
                    return null;
                }
                this.mBackStack.add(Integer.valueOf(id));
                return destination;
            }
            beginTransaction.addToBackStack(generateBackStackName(this.mBackStack.size() + 1, id));
        }
        z = true;
        if (extras instanceof Extras) {
        }
        beginTransaction.setReorderingAllowed(true);
        beginTransaction.commit();
        if (z) {
        }
    }

    public Bundle onSaveState() {
        Bundle bundle = new Bundle();
        int[] iArr = new int[this.mBackStack.size()];
        Iterator it = this.mBackStack.iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            iArr[i] = ((Integer) it.next()).intValue();
            i = i2;
        }
        bundle.putIntArray(KEY_BACK_STACK_IDS, iArr);
        return bundle;
    }

    public void onRestoreState(Bundle bundle) {
        if (bundle != null) {
            int[] intArray = bundle.getIntArray(KEY_BACK_STACK_IDS);
            if (intArray != null) {
                this.mBackStack.clear();
                for (int valueOf : intArray) {
                    this.mBackStack.add(Integer.valueOf(valueOf));
                }
            }
        }
    }

    private String generateBackStackName(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("-");
        sb.append(i2);
        return sb.toString();
    }

    private int getDestId(String str) {
        String[] split = str != null ? str.split("-") : new String[0];
        String str2 = "Invalid back stack entry on the NavHostFragment's back stack - use getChildFragmentManager() if you need to do custom FragmentTransactions from within Fragments created via your navigation graph.";
        if (split.length == 2) {
            try {
                Integer.parseInt(split[0]);
                return Integer.parseInt(split[1]);
            } catch (NumberFormatException unused) {
                throw new IllegalStateException(str2);
            }
        } else {
            throw new IllegalStateException(str2);
        }
    }
}
