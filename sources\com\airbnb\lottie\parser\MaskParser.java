package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.model.content.Mask.MaskMode;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.Logger;
import java.io.IOException;

class MaskParser {
    private MaskParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0071  */
    static Mask parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        boolean z;
        jsonReader.beginObject();
        MaskMode maskMode = null;
        boolean z2 = false;
        AnimatableShapeValue animatableShapeValue = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            int hashCode = nextName.hashCode();
            char c = 65535;
            if (hashCode != 111) {
                if (hashCode != 3588) {
                    if (hashCode != 104433) {
                        if (hashCode == 3357091 && nextName.equals("mode")) {
                            z = false;
                            if (!z) {
                                String nextString = jsonReader.nextString();
                                int hashCode2 = nextString.hashCode();
                                if (hashCode2 != 97) {
                                    if (hashCode2 != 105) {
                                        if (hashCode2 != 110) {
                                            if (hashCode2 == 115 && nextString.equals("s")) {
                                                c = 1;
                                            }
                                        } else if (nextString.equals("n")) {
                                            c = 2;
                                        }
                                    } else if (nextString.equals("i")) {
                                        c = 3;
                                    }
                                } else if (nextString.equals("a")) {
                                    c = 0;
                                }
                                if (c == 0) {
                                    maskMode = MaskMode.MASK_MODE_ADD;
                                } else if (c == 1) {
                                    maskMode = MaskMode.MASK_MODE_SUBTRACT;
                                } else if (c == 2) {
                                    maskMode = MaskMode.MASK_MODE_NONE;
                                } else if (c != 3) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Unknown mask mode ");
                                    sb.append(nextName);
                                    sb.append(". Defaulting to Add.");
                                    Logger.warning(sb.toString());
                                    maskMode = MaskMode.MASK_MODE_ADD;
                                } else {
                                    lottieComposition.addWarning("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                                    maskMode = MaskMode.MASK_MODE_INTERSECT;
                                }
                            } else if (z) {
                                animatableShapeValue = AnimatableValueParser.parseShapeData(jsonReader, lottieComposition);
                            } else if (z) {
                                animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                            } else if (!z) {
                                jsonReader.skipValue();
                            } else {
                                z2 = jsonReader.nextBoolean();
                            }
                        }
                    } else if (nextName.equals("inv")) {
                        z = true;
                        if (!z) {
                        }
                    }
                } else if (nextName.equals("pt")) {
                    z = true;
                    if (!z) {
                    }
                }
            } else if (nextName.equals("o")) {
                z = true;
                if (!z) {
                }
            }
            z = true;
            if (!z) {
            }
        }
        jsonReader.endObject();
        return new Mask(maskMode, animatableShapeValue, animatableIntegerValue, z2);
    }
}
