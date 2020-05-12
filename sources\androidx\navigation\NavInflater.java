package androidx.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.navigation.NavArgument.Builder;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class NavInflater {
    static final String APPLICATION_ID_PLACEHOLDER = "${applicationId}";
    private static final String TAG_ACTION = "action";
    private static final String TAG_ARGUMENT = "argument";
    private static final String TAG_DEEP_LINK = "deepLink";
    private static final String TAG_INCLUDE = "include";
    private static final ThreadLocal<TypedValue> sTmpValue = new ThreadLocal<>();
    private Context mContext;
    private NavigatorProvider mNavigatorProvider;

    public NavInflater(Context context, NavigatorProvider navigatorProvider) {
        this.mContext = context;
        this.mNavigatorProvider = navigatorProvider;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0049 A[Catch:{ Exception -> 0x0053, all -> 0x0051 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b A[Catch:{ Exception -> 0x0053, all -> 0x0051 }] */
    public NavGraph inflate(int i) {
        int next;
        Resources resources = this.mContext.getResources();
        XmlResourceParser xml = resources.getXml(i);
        AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
        while (true) {
            try {
                next = xml.next();
                if (next == 2 || next == 1) {
                    if (next != 2) {
                        String name = xml.getName();
                        NavDestination inflate = inflate(resources, xml, asAttributeSet, i);
                        if (inflate instanceof NavGraph) {
                            NavGraph navGraph = (NavGraph) inflate;
                            xml.close();
                            return navGraph;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Root element <");
                        sb.append(name);
                        sb.append("> did not inflate into a NavGraph");
                        throw new IllegalArgumentException(sb.toString());
                    }
                    throw new XmlPullParserException("No start tag found");
                }
            } catch (Exception e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Exception inflating ");
                sb2.append(resources.getResourceName(i));
                sb2.append(" line ");
                sb2.append(xml.getLineNumber());
                throw new RuntimeException(sb2.toString(), e);
            } catch (Throwable th) {
                xml.close();
                throw th;
            }
        }
        if (next != 2) {
        }
    }

    private NavDestination inflate(Resources resources, XmlResourceParser xmlResourceParser, AttributeSet attributeSet, int i) throws XmlPullParserException, IOException {
        NavDestination createDestination = this.mNavigatorProvider.getNavigator(xmlResourceParser.getName()).createDestination();
        createDestination.onInflate(this.mContext, attributeSet);
        int depth = xmlResourceParser.getDepth() + 1;
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1) {
                break;
            }
            int depth2 = xmlResourceParser.getDepth();
            if (depth2 < depth && next == 3) {
                break;
            } else if (next == 2 && depth2 <= depth) {
                String name = xmlResourceParser.getName();
                if (TAG_ARGUMENT.equals(name)) {
                    inflateArgumentForDestination(resources, createDestination, attributeSet, i);
                } else if (TAG_DEEP_LINK.equals(name)) {
                    inflateDeepLink(resources, createDestination, attributeSet);
                } else if ("action".equals(name)) {
                    inflateAction(resources, createDestination, attributeSet, xmlResourceParser, i);
                } else if (TAG_INCLUDE.equals(name) && (createDestination instanceof NavGraph)) {
                    TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.NavInclude);
                    ((NavGraph) createDestination).addDestination(inflate(obtainAttributes.getResourceId(R.styleable.NavInclude_graph, 0)));
                    obtainAttributes.recycle();
                } else if (createDestination instanceof NavGraph) {
                    ((NavGraph) createDestination).addDestination(inflate(resources, xmlResourceParser, attributeSet, i));
                }
            }
        }
        return createDestination;
    }

    private void inflateArgumentForDestination(Resources resources, NavDestination navDestination, AttributeSet attributeSet, int i) throws XmlPullParserException {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.NavArgument);
        String string = obtainAttributes.getString(R.styleable.NavArgument_android_name);
        if (string != null) {
            navDestination.addArgument(string, inflateArgument(obtainAttributes, resources, i));
            obtainAttributes.recycle();
            return;
        }
        throw new XmlPullParserException("Arguments must have a name");
    }

    private void inflateArgumentForBundle(Resources resources, Bundle bundle, AttributeSet attributeSet, int i) throws XmlPullParserException {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.NavArgument);
        String string = obtainAttributes.getString(R.styleable.NavArgument_android_name);
        if (string != null) {
            NavArgument inflateArgument = inflateArgument(obtainAttributes, resources, i);
            if (inflateArgument.isDefaultValuePresent()) {
                inflateArgument.putDefaultValue(string, bundle);
            }
            obtainAttributes.recycle();
            return;
        }
        throw new XmlPullParserException("Arguments must have a name");
    }

    private NavArgument inflateArgument(TypedArray typedArray, Resources resources, int i) throws XmlPullParserException {
        Builder builder = new Builder();
        boolean z = false;
        builder.setIsNullable(typedArray.getBoolean(R.styleable.NavArgument_nullable, false));
        TypedValue typedValue = (TypedValue) sTmpValue.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            sTmpValue.set(typedValue);
        }
        String string = typedArray.getString(R.styleable.NavArgument_argType);
        Object obj = null;
        NavType<Integer> fromArgType = string != null ? NavType.fromArgType(string, resources.getResourcePackageName(i)) : null;
        if (typedArray.getValue(R.styleable.NavArgument_android_defaultValue, typedValue)) {
            String str = "' for ";
            String str2 = "unsupported value '";
            if (fromArgType == NavType.ReferenceType) {
                if (typedValue.resourceId != 0) {
                    obj = Integer.valueOf(typedValue.resourceId);
                } else if (typedValue.type == 16 && typedValue.data == 0) {
                    obj = Integer.valueOf(0);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(typedValue.string);
                    sb.append(str);
                    sb.append(fromArgType.getName());
                    sb.append(". Must be a reference to a resource.");
                    throw new XmlPullParserException(sb.toString());
                }
            } else if (typedValue.resourceId != 0) {
                if (fromArgType == null) {
                    fromArgType = NavType.ReferenceType;
                    obj = Integer.valueOf(typedValue.resourceId);
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(typedValue.string);
                    sb2.append(str);
                    sb2.append(fromArgType.getName());
                    sb2.append(". You must use a \"");
                    sb2.append(NavType.ReferenceType.getName());
                    sb2.append("\" type to reference other resources.");
                    throw new XmlPullParserException(sb2.toString());
                }
            } else if (fromArgType == NavType.StringType) {
                obj = typedArray.getString(R.styleable.NavArgument_android_defaultValue);
            } else {
                int i2 = typedValue.type;
                if (i2 == 3) {
                    String charSequence = typedValue.string.toString();
                    if (fromArgType == null) {
                        fromArgType = NavType.inferFromValue(charSequence);
                    }
                    obj = fromArgType.parseValue(charSequence);
                } else if (i2 == 4) {
                    fromArgType = checkNavType(typedValue, fromArgType, NavType.FloatType, string, "float");
                    obj = Float.valueOf(typedValue.getFloat());
                } else if (i2 == 5) {
                    fromArgType = checkNavType(typedValue, fromArgType, NavType.IntType, string, "dimension");
                    obj = Integer.valueOf((int) typedValue.getDimension(resources.getDisplayMetrics()));
                } else if (i2 == 18) {
                    fromArgType = checkNavType(typedValue, fromArgType, NavType.BoolType, string, "boolean");
                    if (typedValue.data != 0) {
                        z = true;
                    }
                    obj = Boolean.valueOf(z);
                } else if (typedValue.type < 16 || typedValue.type > 31) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("unsupported argument type ");
                    sb3.append(typedValue.type);
                    throw new XmlPullParserException(sb3.toString());
                } else {
                    fromArgType = checkNavType(typedValue, fromArgType, NavType.IntType, string, "integer");
                    obj = Integer.valueOf(typedValue.data);
                }
            }
        }
        if (obj != null) {
            builder.setDefaultValue(obj);
        }
        if (fromArgType != null) {
            builder.setType(fromArgType);
        }
        return builder.build();
    }

    private static NavType checkNavType(TypedValue typedValue, NavType navType, NavType navType2, String str, String str2) throws XmlPullParserException {
        if (navType == null || navType == navType2) {
            return navType != null ? navType : navType2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Type is ");
        sb.append(str);
        sb.append(" but found ");
        sb.append(str2);
        sb.append(": ");
        sb.append(typedValue.data);
        throw new XmlPullParserException(sb.toString());
    }

    private void inflateDeepLink(Resources resources, NavDestination navDestination, AttributeSet attributeSet) {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.NavDeepLink);
        String string = obtainAttributes.getString(R.styleable.NavDeepLink_uri);
        if (!TextUtils.isEmpty(string)) {
            navDestination.addDeepLink(string.replace(APPLICATION_ID_PLACEHOLDER, this.mContext.getPackageName()));
            obtainAttributes.recycle();
            return;
        }
        throw new IllegalArgumentException("Every <deepLink> must include an app:uri");
    }

    private void inflateAction(Resources resources, NavDestination navDestination, AttributeSet attributeSet, XmlResourceParser xmlResourceParser, int i) throws IOException, XmlPullParserException {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, R.styleable.NavAction);
        int resourceId = obtainAttributes.getResourceId(R.styleable.NavAction_android_id, 0);
        NavAction navAction = new NavAction(obtainAttributes.getResourceId(R.styleable.NavAction_destination, 0));
        NavOptions.Builder builder = new NavOptions.Builder();
        builder.setLaunchSingleTop(obtainAttributes.getBoolean(R.styleable.NavAction_launchSingleTop, false));
        builder.setPopUpTo(obtainAttributes.getResourceId(R.styleable.NavAction_popUpTo, -1), obtainAttributes.getBoolean(R.styleable.NavAction_popUpToInclusive, false));
        builder.setEnterAnim(obtainAttributes.getResourceId(R.styleable.NavAction_enterAnim, -1));
        builder.setExitAnim(obtainAttributes.getResourceId(R.styleable.NavAction_exitAnim, -1));
        builder.setPopEnterAnim(obtainAttributes.getResourceId(R.styleable.NavAction_popEnterAnim, -1));
        builder.setPopExitAnim(obtainAttributes.getResourceId(R.styleable.NavAction_popExitAnim, -1));
        navAction.setNavOptions(builder.build());
        Bundle bundle = new Bundle();
        int depth = xmlResourceParser.getDepth() + 1;
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1) {
                break;
            }
            int depth2 = xmlResourceParser.getDepth();
            if (depth2 < depth && next == 3) {
                break;
            } else if (next == 2 && depth2 <= depth) {
                if (TAG_ARGUMENT.equals(xmlResourceParser.getName())) {
                    inflateArgumentForBundle(resources, bundle, attributeSet, i);
                }
            }
        }
        if (!bundle.isEmpty()) {
            navAction.setDefaultArguments(bundle);
        }
        navDestination.putAction(resourceId, navAction);
        obtainAttributes.recycle();
    }
}
