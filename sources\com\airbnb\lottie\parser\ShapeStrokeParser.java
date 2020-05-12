package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.model.content.ShapeStroke.LineCapType;
import com.airbnb.lottie.model.content.ShapeStroke.LineJoinType;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonReader.Options;
import com.worklight.wlclient.api.SecurityUtils;
import java.io.IOException;
import java.util.ArrayList;

class ShapeStrokeParser {
    private static final Options DASH_PATTERN_NAMES = Options.of("n", SecurityUtils.VERSION_LABEL);
    private static Options NAMES = Options.of("nm", "c", "w", "o", "lc", "lj", "ml", "hd", "d");

    private ShapeStrokeParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x009e  */
    static ShapeStroke parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        char c;
        int i;
        JsonReader jsonReader2 = jsonReader;
        ArrayList arrayList = new ArrayList();
        float f = 0.0f;
        String str = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableColorValue animatableColorValue = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        LineCapType lineCapType = null;
        LineJoinType lineJoinType = null;
        boolean z = false;
        while (jsonReader.hasNext()) {
            int i2 = 1;
            switch (jsonReader2.selectName(NAMES)) {
                case 0:
                    LottieComposition lottieComposition2 = lottieComposition;
                    str = jsonReader.nextString();
                    break;
                case 1:
                    LottieComposition lottieComposition3 = lottieComposition;
                    animatableColorValue = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    break;
                case 2:
                    LottieComposition lottieComposition4 = lottieComposition;
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 3:
                    LottieComposition lottieComposition5 = lottieComposition;
                    animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    break;
                case 4:
                    LottieComposition lottieComposition6 = lottieComposition;
                    lineCapType = LineCapType.values()[jsonReader.nextInt() - 1];
                    break;
                case 5:
                    LottieComposition lottieComposition7 = lottieComposition;
                    lineJoinType = LineJoinType.values()[jsonReader.nextInt() - 1];
                    break;
                case 6:
                    LottieComposition lottieComposition8 = lottieComposition;
                    f = (float) jsonReader.nextDouble();
                    break;
                case 7:
                    LottieComposition lottieComposition9 = lottieComposition;
                    z = jsonReader.nextBoolean();
                    break;
                case 8:
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String str2 = null;
                        AnimatableFloatValue animatableFloatValue3 = null;
                        while (jsonReader.hasNext()) {
                            int selectName = jsonReader2.selectName(DASH_PATTERN_NAMES);
                            if (selectName == 0) {
                                str2 = jsonReader.nextString();
                            } else if (selectName != i2) {
                                jsonReader.skipName();
                                jsonReader.skipValue();
                            } else {
                                animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                            }
                        }
                        jsonReader.endObject();
                        int hashCode = str2.hashCode();
                        if (hashCode != 100) {
                            if (hashCode != 103) {
                                if (hashCode == 111 && str2.equals("o")) {
                                    c = 0;
                                    if (c == 0) {
                                        i = 1;
                                        if (c == 1 || c == 2) {
                                            lottieComposition.setHasDashPattern(true);
                                            arrayList.add(animatableFloatValue3);
                                        } else {
                                            LottieComposition lottieComposition10 = lottieComposition;
                                        }
                                    } else {
                                        LottieComposition lottieComposition11 = lottieComposition;
                                        i = 1;
                                        animatableFloatValue = animatableFloatValue3;
                                    }
                                    i2 = i;
                                }
                            } else if (str2.equals("g")) {
                                c = 2;
                                if (c == 0) {
                                }
                                i2 = i;
                            }
                        } else if (str2.equals("d")) {
                            c = 1;
                            if (c == 0) {
                            }
                            i2 = i;
                        }
                        c = 65535;
                        if (c == 0) {
                        }
                        i2 = i;
                    }
                    LottieComposition lottieComposition12 = lottieComposition;
                    int i3 = i2;
                    jsonReader.endArray();
                    if (arrayList.size() != i3) {
                        break;
                    } else {
                        arrayList.add(arrayList.get(0));
                        break;
                    }
                default:
                    LottieComposition lottieComposition13 = lottieComposition;
                    jsonReader.skipValue();
                    break;
            }
        }
        ShapeStroke shapeStroke = new ShapeStroke(str, animatableFloatValue, arrayList, animatableColorValue, animatableIntegerValue, animatableFloatValue2, lineCapType, lineJoinType, f, z);
        return shapeStroke;
    }
}
