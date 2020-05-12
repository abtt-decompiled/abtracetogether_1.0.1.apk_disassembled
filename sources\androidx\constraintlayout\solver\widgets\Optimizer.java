package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour;

public class Optimizer {
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    static final int FLAG_USE_OPTIMIZE = 0;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_GROUPS = 32;
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_STANDARD = 7;
    static boolean[] flags = new boolean[3];

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_PARENT) {
            int i = constraintWidget.mLeft.mMargin;
            int width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(i, width);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_PARENT) {
            int i2 = constraintWidget.mTop.mMargin;
            int height = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i2);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(i2, height);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x003d A[RETURN] */
    private static boolean optimizableMatchConstraint(ConstraintWidget constraintWidget, int i) {
        if (constraintWidget.mListDimensionBehaviors[i] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        char c = 1;
        if (constraintWidget.mDimensionRatio != 0.0f) {
            DimensionBehaviour[] dimensionBehaviourArr = constraintWidget.mListDimensionBehaviors;
            if (i != 0) {
                c = 0;
            }
            if (dimensionBehaviourArr[c] == DimensionBehaviour.MATCH_CONSTRAINT) {
            }
            return false;
        }
        if (i == 0) {
            if (constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.mMatchConstraintMinWidth == 0 && constraintWidget.mMatchConstraintMaxWidth == 0) {
                return true;
            }
            return false;
        } else if (constraintWidget.mMatchConstraintDefaultHeight != 0 || constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
            return false;
        }
        return true;
    }

    static void analyze(int i, ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        constraintWidget.updateResolutionNodes();
        ResolutionAnchor resolutionNode = constraintWidget2.mLeft.getResolutionNode();
        ResolutionAnchor resolutionNode2 = constraintWidget2.mTop.getResolutionNode();
        ResolutionAnchor resolutionNode3 = constraintWidget2.mRight.getResolutionNode();
        ResolutionAnchor resolutionNode4 = constraintWidget2.mBottom.getResolutionNode();
        boolean z = (i & 8) == 8;
        boolean z2 = constraintWidget2.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 0);
        if (!(resolutionNode.type == 4 || resolutionNode3.type == 4)) {
            if (constraintWidget2.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED || (z2 && constraintWidget.getVisibility() == 8)) {
                if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (z) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                    }
                } else if (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mRight.mTarget == null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (z) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                    }
                } else if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget != null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                    if (z) {
                        resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                    }
                } else if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                    resolutionNode.setType(2);
                    resolutionNode3.setType(2);
                    if (z) {
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                        resolutionNode.setOpposite(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                        resolutionNode3.setOpposite(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.setOpposite(resolutionNode3, (float) (-constraintWidget.getWidth()));
                        resolutionNode3.setOpposite(resolutionNode, (float) constraintWidget.getWidth());
                    }
                }
            } else if (z2) {
                int width = constraintWidget.getWidth();
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                    if (z) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, width);
                    }
                } else if (constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget != null) {
                    if (constraintWidget2.mLeft.mTarget != null || constraintWidget2.mRight.mTarget == null) {
                        if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                            if (z) {
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                            }
                            if (constraintWidget2.mDimensionRatio == 0.0f) {
                                resolutionNode.setType(3);
                                resolutionNode3.setType(3);
                                resolutionNode.setOpposite(resolutionNode3, 0.0f);
                                resolutionNode3.setOpposite(resolutionNode, 0.0f);
                            } else {
                                resolutionNode.setType(2);
                                resolutionNode3.setType(2);
                                resolutionNode.setOpposite(resolutionNode3, (float) (-width));
                                resolutionNode3.setOpposite(resolutionNode, (float) width);
                                constraintWidget2.setWidth(width);
                            }
                        }
                    } else if (z) {
                        resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.dependsOn(resolutionNode3, -width);
                    }
                } else if (z) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode3.dependsOn(resolutionNode, width);
                }
            }
        }
        boolean z3 = constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 1);
        if (resolutionNode2.type != 4 && resolutionNode4.type != 4) {
            if (constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED || (z3 && constraintWidget.getVisibility() == 8)) {
                if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (z) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaseline.mTarget != null) {
                        constraintWidget2.mBaseline.getResolutionNode().setType(1);
                        resolutionNode2.dependsOn(1, constraintWidget2.mBaseline.getResolutionNode(), -constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget == null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (z) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget != null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (z) {
                        resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode2.dependsOn(resolutionNode4, -constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                    resolutionNode2.setType(2);
                    resolutionNode4.setType(2);
                    if (z) {
                        resolutionNode2.setOpposite(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                        resolutionNode4.setOpposite(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                    } else {
                        resolutionNode2.setOpposite(resolutionNode4, (float) (-constraintWidget.getHeight()));
                        resolutionNode4.setOpposite(resolutionNode2, (float) constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                }
            } else if (z3) {
                int height = constraintWidget.getHeight();
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                    if (z) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, height);
                    }
                } else if (constraintWidget2.mTop.mTarget == null || constraintWidget2.mBottom.mTarget != null) {
                    if (constraintWidget2.mTop.mTarget != null || constraintWidget2.mBottom.mTarget == null) {
                        if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                            if (z) {
                                constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                            }
                            if (constraintWidget2.mDimensionRatio == 0.0f) {
                                resolutionNode2.setType(3);
                                resolutionNode4.setType(3);
                                resolutionNode2.setOpposite(resolutionNode4, 0.0f);
                                resolutionNode4.setOpposite(resolutionNode2, 0.0f);
                                return;
                            }
                            resolutionNode2.setType(2);
                            resolutionNode4.setType(2);
                            resolutionNode2.setOpposite(resolutionNode4, (float) (-height));
                            resolutionNode4.setOpposite(resolutionNode2, (float) height);
                            constraintWidget2.setHeight(height);
                            if (constraintWidget2.mBaselineDistance > 0) {
                                constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                            }
                        }
                    } else if (z) {
                        resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode2.dependsOn(resolutionNode4, -height);
                    }
                } else if (z) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionNode4.dependsOn(resolutionNode2, height);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        if (r7.mHorizontalChainStyle == 2) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
        if (r7.mVerticalChainStyle == 2) goto L_0x0034;
     */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x01db  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0103  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0106  */
    static boolean applyChainOptimized(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        float f;
        int i3;
        int i4;
        float f2;
        ConstraintWidget constraintWidget;
        boolean z3;
        int i5;
        LinearSystem linearSystem2 = linearSystem;
        int i6 = i;
        ChainHead chainHead2 = chainHead;
        ConstraintWidget constraintWidget2 = chainHead2.mFirst;
        ConstraintWidget constraintWidget3 = chainHead2.mLast;
        ConstraintWidget constraintWidget4 = chainHead2.mFirstVisibleWidget;
        ConstraintWidget constraintWidget5 = chainHead2.mLastVisibleWidget;
        ConstraintWidget constraintWidget6 = chainHead2.mHead;
        float f3 = chainHead2.mTotalWeight;
        ConstraintWidget constraintWidget7 = chainHead2.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget8 = chainHead2.mLastMatchConstraintWidget;
        DimensionBehaviour dimensionBehaviour = constraintWidgetContainer.mListDimensionBehaviors[i6];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.WRAP_CONTENT;
        if (i6 == 0) {
            z2 = constraintWidget6.mHorizontalChainStyle == 0;
            z = constraintWidget6.mHorizontalChainStyle == 1;
        } else {
            z2 = constraintWidget6.mVerticalChainStyle == 0;
            z = constraintWidget6.mVerticalChainStyle == 1;
        }
        boolean z4 = true;
        ConstraintWidget constraintWidget9 = constraintWidget2;
        int i7 = 0;
        boolean z5 = false;
        int i8 = 0;
        float f4 = 0.0f;
        float f5 = 0.0f;
        while (!z5) {
            if (constraintWidget9.getVisibility() != 8) {
                i8++;
                if (i6 == 0) {
                    i5 = constraintWidget9.getWidth();
                } else {
                    i5 = constraintWidget9.getHeight();
                }
                f4 += (float) i5;
                if (constraintWidget9 != constraintWidget4) {
                    f4 += (float) constraintWidget9.mListAnchors[i2].getMargin();
                }
                if (constraintWidget9 != constraintWidget5) {
                    f4 += (float) constraintWidget9.mListAnchors[i2 + 1].getMargin();
                }
                f5 = f5 + ((float) constraintWidget9.mListAnchors[i2].getMargin()) + ((float) constraintWidget9.mListAnchors[i2 + 1].getMargin());
            }
            ConstraintAnchor constraintAnchor = constraintWidget9.mListAnchors[i2];
            if (constraintWidget9.getVisibility() != 8 && constraintWidget9.mListDimensionBehaviors[i6] == DimensionBehaviour.MATCH_CONSTRAINT) {
                i7++;
                if (i6 != 0) {
                    z3 = false;
                    if (constraintWidget9.mMatchConstraintDefaultHeight != 0) {
                        return false;
                    }
                    if (constraintWidget9.mMatchConstraintMinHeight == 0) {
                        if (constraintWidget9.mMatchConstraintMaxHeight != 0) {
                        }
                    }
                    return z3;
                } else if (constraintWidget9.mMatchConstraintDefaultWidth != 0) {
                    return false;
                } else {
                    z3 = false;
                    if (!(constraintWidget9.mMatchConstraintMinWidth == 0 && constraintWidget9.mMatchConstraintMaxWidth == 0)) {
                        return false;
                    }
                }
                if (constraintWidget9.mDimensionRatio != 0.0f) {
                    return z3;
                }
            }
            ConstraintAnchor constraintAnchor2 = constraintWidget9.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor2 != null) {
                ConstraintWidget constraintWidget10 = constraintAnchor2.mOwner;
                if (constraintWidget10.mListAnchors[i2].mTarget != null && constraintWidget10.mListAnchors[i2].mTarget.mOwner == constraintWidget9) {
                    constraintWidget = constraintWidget10;
                    if (constraintWidget == null) {
                        constraintWidget9 = constraintWidget;
                    } else {
                        z5 = true;
                    }
                }
            }
            constraintWidget = null;
            if (constraintWidget == null) {
            }
        }
        ResolutionAnchor resolutionNode = constraintWidget2.mListAnchors[i2].getResolutionNode();
        int i9 = i2 + 1;
        ResolutionAnchor resolutionNode2 = constraintWidget3.mListAnchors[i9].getResolutionNode();
        if (resolutionNode.target == null || resolutionNode2.target == null) {
            return false;
        }
        ConstraintWidget constraintWidget11 = constraintWidget2;
        if (resolutionNode.target.state != 1 || resolutionNode2.target.state != 1) {
            return false;
        }
        if (i7 > 0 && i7 != i8) {
            return false;
        }
        if (z4 || z2 || z) {
            f = constraintWidget4 != null ? (float) constraintWidget4.mListAnchors[i2].getMargin() : 0.0f;
            if (constraintWidget5 != null) {
                f += (float) constraintWidget5.mListAnchors[i9].getMargin();
            }
        } else {
            f = 0.0f;
        }
        float f6 = resolutionNode.target.resolvedOffset;
        float f7 = resolutionNode2.target.resolvedOffset;
        float f8 = (f6 < f7 ? f7 - f6 : f6 - f7) - f4;
        if (i7 <= 0 || i7 != i8) {
            if (f8 < 0.0f) {
                z4 = true;
                z2 = false;
                z = false;
            }
            if (z4) {
                ConstraintWidget constraintWidget12 = constraintWidget11;
                float biasPercent = f6 + ((f8 - f) * constraintWidget12.getBiasPercent(i6));
                while (true) {
                    ConstraintWidget constraintWidget13 = constraintWidget12;
                    if (constraintWidget13 == null) {
                        break;
                    }
                    if (LinearSystem.sMetrics != null) {
                        Metrics metrics = LinearSystem.sMetrics;
                        metrics.nonresolvedWidgets--;
                        Metrics metrics2 = LinearSystem.sMetrics;
                        metrics2.resolvedWidgets++;
                        Metrics metrics3 = LinearSystem.sMetrics;
                        metrics3.chainConnectionResolved++;
                    }
                    constraintWidget12 = constraintWidget13.mNextChainWidget[i6];
                    if (constraintWidget12 != null || constraintWidget13 == constraintWidget3) {
                        if (i6 == 0) {
                            i4 = constraintWidget13.getWidth();
                        } else {
                            i4 = constraintWidget13.getHeight();
                        }
                        float f9 = (float) i4;
                        float margin = biasPercent + ((float) constraintWidget13.mListAnchors[i2].getMargin());
                        constraintWidget13.mListAnchors[i2].getResolutionNode().resolve(resolutionNode.resolvedTarget, margin);
                        float f10 = margin + f9;
                        constraintWidget13.mListAnchors[i9].getResolutionNode().resolve(resolutionNode.resolvedTarget, f10);
                        constraintWidget13.mListAnchors[i2].getResolutionNode().addResolvedValue(linearSystem2);
                        constraintWidget13.mListAnchors[i9].getResolutionNode().addResolvedValue(linearSystem2);
                        biasPercent = f10 + ((float) constraintWidget13.mListAnchors[i9].getMargin());
                    }
                }
            } else {
                ConstraintWidget constraintWidget14 = constraintWidget11;
                if (z2 || z) {
                    if (z2 || z) {
                        f8 -= f;
                    }
                    float f11 = f8 / ((float) (i8 + 1));
                    if (z) {
                        f11 = f8 / (i8 > 1 ? (float) (i8 - 1) : 2.0f);
                    }
                    float f12 = constraintWidget14.getVisibility() != 8 ? f6 + f11 : f6;
                    if (z && i8 > 1) {
                        f12 = ((float) constraintWidget4.mListAnchors[i2].getMargin()) + f6;
                    }
                    if (z2 && constraintWidget4 != null) {
                        f12 += (float) constraintWidget4.mListAnchors[i2].getMargin();
                    }
                    while (constraintWidget14 != null) {
                        if (LinearSystem.sMetrics != null) {
                            Metrics metrics4 = LinearSystem.sMetrics;
                            metrics4.nonresolvedWidgets--;
                            Metrics metrics5 = LinearSystem.sMetrics;
                            metrics5.resolvedWidgets++;
                            Metrics metrics6 = LinearSystem.sMetrics;
                            metrics6.chainConnectionResolved++;
                        }
                        ConstraintWidget constraintWidget15 = constraintWidget14.mNextChainWidget[i6];
                        if (constraintWidget15 != null || constraintWidget14 == constraintWidget3) {
                            if (i6 == 0) {
                                i3 = constraintWidget14.getWidth();
                            } else {
                                i3 = constraintWidget14.getHeight();
                            }
                            float f13 = (float) i3;
                            if (constraintWidget14 != constraintWidget4) {
                                f12 += (float) constraintWidget14.mListAnchors[i2].getMargin();
                            }
                            constraintWidget14.mListAnchors[i2].getResolutionNode().resolve(resolutionNode.resolvedTarget, f12);
                            constraintWidget14.mListAnchors[i9].getResolutionNode().resolve(resolutionNode.resolvedTarget, f12 + f13);
                            constraintWidget14.mListAnchors[i2].getResolutionNode().addResolvedValue(linearSystem2);
                            constraintWidget14.mListAnchors[i9].getResolutionNode().addResolvedValue(linearSystem2);
                            f12 += f13 + ((float) constraintWidget14.mListAnchors[i9].getMargin());
                            if (constraintWidget15 != null) {
                                if (constraintWidget15.getVisibility() != 8) {
                                    f12 += f11;
                                }
                                constraintWidget14 = constraintWidget15;
                            }
                        }
                        constraintWidget14 = constraintWidget15;
                    }
                }
            }
            return true;
        } else if (constraintWidget9.getParent() != null && constraintWidget9.getParent().mListDimensionBehaviors[i6] == DimensionBehaviour.WRAP_CONTENT) {
            return false;
        } else {
            float f14 = (f8 + f4) - f5;
            ConstraintWidget constraintWidget16 = constraintWidget11;
            while (constraintWidget16 != null) {
                if (LinearSystem.sMetrics != null) {
                    Metrics metrics7 = LinearSystem.sMetrics;
                    metrics7.nonresolvedWidgets--;
                    Metrics metrics8 = LinearSystem.sMetrics;
                    metrics8.resolvedWidgets++;
                    Metrics metrics9 = LinearSystem.sMetrics;
                    metrics9.chainConnectionResolved++;
                }
                ConstraintWidget constraintWidget17 = constraintWidget16.mNextChainWidget[i6];
                if (constraintWidget17 != null || constraintWidget16 == constraintWidget3) {
                    float f15 = f14 / ((float) i7);
                    if (f3 > 0.0f) {
                        if (constraintWidget16.mWeight[i6] == -1.0f) {
                            f2 = 0.0f;
                            if (constraintWidget16.getVisibility() == 8) {
                                f2 = 0.0f;
                            }
                            float margin2 = f6 + ((float) constraintWidget16.mListAnchors[i2].getMargin());
                            constraintWidget16.mListAnchors[i2].getResolutionNode().resolve(resolutionNode.resolvedTarget, margin2);
                            float f16 = margin2 + f2;
                            constraintWidget16.mListAnchors[i9].getResolutionNode().resolve(resolutionNode.resolvedTarget, f16);
                            constraintWidget16.mListAnchors[i2].getResolutionNode().addResolvedValue(linearSystem2);
                            constraintWidget16.mListAnchors[i9].getResolutionNode().addResolvedValue(linearSystem2);
                            f6 = f16 + ((float) constraintWidget16.mListAnchors[i9].getMargin());
                        } else {
                            f15 = (constraintWidget16.mWeight[i6] * f14) / f3;
                        }
                    }
                    f2 = f15;
                    if (constraintWidget16.getVisibility() == 8) {
                    }
                    float margin22 = f6 + ((float) constraintWidget16.mListAnchors[i2].getMargin());
                    constraintWidget16.mListAnchors[i2].getResolutionNode().resolve(resolutionNode.resolvedTarget, margin22);
                    float f162 = margin22 + f2;
                    constraintWidget16.mListAnchors[i9].getResolutionNode().resolve(resolutionNode.resolvedTarget, f162);
                    constraintWidget16.mListAnchors[i2].getResolutionNode().addResolvedValue(linearSystem2);
                    constraintWidget16.mListAnchors[i9].getResolutionNode().addResolvedValue(linearSystem2);
                    f6 = f162 + ((float) constraintWidget16.mListAnchors[i9].getMargin());
                }
                constraintWidget16 = constraintWidget17;
            }
            return true;
        }
    }

    static void setOptimizedWidget(ConstraintWidget constraintWidget, int i, int i2) {
        int i3 = i * 2;
        int i4 = i3 + 1;
        constraintWidget.mListAnchors[i3].getResolutionNode().resolvedTarget = constraintWidget.getParent().mLeft.getResolutionNode();
        constraintWidget.mListAnchors[i3].getResolutionNode().resolvedOffset = (float) i2;
        constraintWidget.mListAnchors[i3].getResolutionNode().state = 1;
        constraintWidget.mListAnchors[i4].getResolutionNode().resolvedTarget = constraintWidget.mListAnchors[i3].getResolutionNode();
        constraintWidget.mListAnchors[i4].getResolutionNode().resolvedOffset = (float) constraintWidget.getLength(i);
        constraintWidget.mListAnchors[i4].getResolutionNode().state = 1;
    }
}
