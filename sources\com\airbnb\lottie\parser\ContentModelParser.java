package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.parser.moshi.JsonReader.Options;
import com.airbnb.lottie.utils.Logger;
import java.io.IOException;

class ContentModelParser {
    private static Options NAMES = Options.of("ty", "d");

    private ContentModelParser() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0094, code lost:
        if (r2.equals("gs") != false) goto L_0x00c0;
     */
    static ContentModel parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        ContentModel contentModel;
        String str;
        jsonReader.beginObject();
        char c = 2;
        int i = 2;
        while (true) {
            contentModel = null;
            if (!jsonReader.hasNext()) {
                str = null;
                break;
            }
            int selectName = jsonReader.selectName(NAMES);
            if (selectName == 0) {
                str = jsonReader.nextString();
                break;
            } else if (selectName != 1) {
                jsonReader.skipName();
                jsonReader.skipValue();
            } else {
                i = jsonReader.nextInt();
            }
        }
        if (str == null) {
            return null;
        }
        switch (str.hashCode()) {
            case 3239:
                if (str.equals("el")) {
                    c = 7;
                    break;
                }
            case 3270:
                if (str.equals("fl")) {
                    c = 3;
                    break;
                }
            case 3295:
                if (str.equals("gf")) {
                    c = 4;
                    break;
                }
            case 3307:
                if (str.equals("gr")) {
                    c = 0;
                    break;
                }
            case 3308:
                break;
            case 3488:
                if (str.equals("mm")) {
                    c = 11;
                    break;
                }
            case 3633:
                if (str.equals("rc")) {
                    c = 8;
                    break;
                }
            case 3646:
                if (str.equals("rp")) {
                    c = 12;
                    break;
                }
            case 3669:
                if (str.equals("sh")) {
                    c = 6;
                    break;
                }
            case 3679:
                if (str.equals("sr")) {
                    c = 10;
                    break;
                }
            case 3681:
                if (str.equals("st")) {
                    c = 1;
                    break;
                }
            case 3705:
                if (str.equals("tm")) {
                    c = 9;
                    break;
                }
            case 3710:
                if (str.equals("tr")) {
                    c = 5;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                contentModel = ShapeGroupParser.parse(jsonReader, lottieComposition);
                break;
            case 1:
                contentModel = ShapeStrokeParser.parse(jsonReader, lottieComposition);
                break;
            case 2:
                contentModel = GradientStrokeParser.parse(jsonReader, lottieComposition);
                break;
            case 3:
                contentModel = ShapeFillParser.parse(jsonReader, lottieComposition);
                break;
            case 4:
                contentModel = GradientFillParser.parse(jsonReader, lottieComposition);
                break;
            case 5:
                contentModel = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                break;
            case 6:
                contentModel = ShapePathParser.parse(jsonReader, lottieComposition);
                break;
            case 7:
                contentModel = CircleShapeParser.parse(jsonReader, lottieComposition, i);
                break;
            case 8:
                contentModel = RectangleShapeParser.parse(jsonReader, lottieComposition);
                break;
            case 9:
                contentModel = ShapeTrimPathParser.parse(jsonReader, lottieComposition);
                break;
            case 10:
                contentModel = PolystarShapeParser.parse(jsonReader, lottieComposition);
                break;
            case 11:
                contentModel = MergePathsParser.parse(jsonReader);
                lottieComposition.addWarning("Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().");
                break;
            case 12:
                contentModel = RepeaterParser.parse(jsonReader, lottieComposition);
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown shape type ");
                sb.append(str);
                Logger.warning(sb.toString());
                break;
        }
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
        }
        jsonReader.endObject();
        return contentModel;
    }
}
