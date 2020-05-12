package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour;
import java.util.ArrayList;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        ChainHead[] chainHeadArr;
        int i2;
        int i3;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i2 = i4;
            i3 = 0;
        } else {
            i3 = 2;
            i2 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i2; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i3, chainHead)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            }
        }
    }

    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r21v1 */
    /* JADX WARNING: type inference failed for: r4v11, types: [androidx.constraintlayout.solver.SolverVariable] */
    /* JADX WARNING: type inference failed for: r21v2 */
    /* JADX WARNING: type inference failed for: r4v12 */
    /* JADX WARNING: type inference failed for: r4v14, types: [androidx.constraintlayout.solver.SolverVariable] */
    /* JADX WARNING: type inference failed for: r4v44 */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        if (r2.mHorizontalChainStyle == 2) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0048, code lost:
        if (r2.mVerticalChainStyle == 2) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004c, code lost:
        r5 = false;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0394  */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x03b6  */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x0486  */
    /* JADX WARNING: Removed duplicated region for block: B:256:0x04bb  */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x04e0  */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x04e5  */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x04eb  */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x04f0  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x04f4  */
    /* JADX WARNING: Removed duplicated region for block: B:276:0x0505  */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x0508  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x0395 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x015f  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x017d  */
    /* JADX WARNING: Unknown variable types count: 3 */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        ConstraintWidget constraintWidget;
        ArrayList<ConstraintWidget> arrayList;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        ConstraintWidget constraintWidget2;
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        ConstraintAnchor constraintAnchor3;
        int i3;
        ConstraintWidget constraintWidget3;
        int i4;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        ConstraintAnchor constraintAnchor4;
        ConstraintWidget constraintWidget4;
        ConstraintWidget constraintWidget5;
        SolverVariable solverVariable5;
        SolverVariable solverVariable6;
        ConstraintAnchor constraintAnchor5;
        float f;
        ArrayList<ConstraintWidget> arrayList2;
        int i5;
        boolean z3;
        boolean z4;
        int i6;
        ConstraintWidget constraintWidget6;
        boolean z5;
        int i7;
        ConstraintWidgetContainer constraintWidgetContainer2 = constraintWidgetContainer;
        LinearSystem linearSystem2 = linearSystem;
        ChainHead chainHead2 = chainHead;
        ConstraintWidget constraintWidget7 = chainHead2.mFirst;
        ConstraintWidget constraintWidget8 = chainHead2.mLast;
        ConstraintWidget constraintWidget9 = chainHead2.mFirstVisibleWidget;
        ConstraintWidget constraintWidget10 = chainHead2.mLastVisibleWidget;
        ConstraintWidget constraintWidget11 = chainHead2.mHead;
        float f2 = chainHead2.mTotalWeight;
        ConstraintWidget constraintWidget12 = chainHead2.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget13 = chainHead2.mLastMatchConstraintWidget;
        boolean z6 = constraintWidgetContainer2.mListDimensionBehaviors[i] == DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            z2 = constraintWidget11.mHorizontalChainStyle == 0;
            z = constraintWidget11.mHorizontalChainStyle == 1;
        } else {
            z2 = constraintWidget11.mVerticalChainStyle == 0;
            z = constraintWidget11.mVerticalChainStyle == 1;
        }
        boolean z7 = true;
        ConstraintWidget constraintWidget14 = constraintWidget7;
        boolean z8 = z;
        boolean z9 = z2;
        boolean z10 = false;
        while (true) {
            constraintWidget = 0;
            if (z10) {
                break;
            }
            ConstraintAnchor constraintAnchor6 = constraintWidget14.mListAnchors[i2];
            int i8 = (z6 || z7) ? 1 : 4;
            int margin = constraintAnchor6.getMargin();
            float f3 = f2;
            if (!(constraintAnchor6.mTarget == null || constraintWidget14 == constraintWidget7)) {
                margin += constraintAnchor6.mTarget.getMargin();
            }
            int i9 = margin;
            if (z7 && constraintWidget14 != constraintWidget7 && constraintWidget14 != constraintWidget9) {
                z3 = z10;
                z4 = z8;
                i6 = 6;
            } else if (!z9 || !z6) {
                z3 = z10;
                i6 = i8;
                z4 = z8;
            } else {
                z3 = z10;
                z4 = z8;
                i6 = 4;
            }
            if (constraintAnchor6.mTarget != null) {
                if (constraintWidget14 == constraintWidget9) {
                    z5 = z9;
                    constraintWidget6 = constraintWidget11;
                    linearSystem2.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i9, 5);
                } else {
                    constraintWidget6 = constraintWidget11;
                    z5 = z9;
                    linearSystem2.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i9, 6);
                }
                linearSystem2.addEquality(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i9, i6);
            } else {
                constraintWidget6 = constraintWidget11;
                z5 = z9;
            }
            if (z6) {
                if (constraintWidget14.getVisibility() == 8 || constraintWidget14.mListDimensionBehaviors[i] != DimensionBehaviour.MATCH_CONSTRAINT) {
                    i7 = 0;
                } else {
                    i7 = 0;
                    linearSystem2.addGreaterThan(constraintWidget14.mListAnchors[i2 + 1].mSolverVariable, constraintWidget14.mListAnchors[i2].mSolverVariable, 0, 5);
                }
                linearSystem2.addGreaterThan(constraintWidget14.mListAnchors[i2].mSolverVariable, constraintWidgetContainer2.mListAnchors[i2].mSolverVariable, i7, 6);
            }
            ConstraintAnchor constraintAnchor7 = constraintWidget14.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor7 != null) {
                ConstraintWidget constraintWidget15 = constraintAnchor7.mOwner;
                if (constraintWidget15.mListAnchors[i2].mTarget != null && constraintWidget15.mListAnchors[i2].mTarget.mOwner == constraintWidget14) {
                    constraintWidget = constraintWidget15;
                }
            }
            if (constraintWidget != null) {
                constraintWidget14 = constraintWidget;
                z10 = z3;
            } else {
                z10 = true;
            }
            z8 = z4;
            f2 = f3;
            z9 = z5;
            constraintWidget11 = constraintWidget6;
        }
        ConstraintWidget constraintWidget16 = constraintWidget11;
        float f4 = f2;
        boolean z11 = z9;
        boolean z12 = z8;
        if (constraintWidget10 != null) {
            int i10 = i2 + 1;
            if (constraintWidget8.mListAnchors[i10].mTarget != null) {
                ConstraintAnchor constraintAnchor8 = constraintWidget10.mListAnchors[i10];
                linearSystem2.addLowerThan(constraintAnchor8.mSolverVariable, constraintWidget8.mListAnchors[i10].mTarget.mSolverVariable, -constraintAnchor8.getMargin(), 5);
                if (z6) {
                    int i11 = i2 + 1;
                    linearSystem2.addGreaterThan(constraintWidgetContainer2.mListAnchors[i11].mSolverVariable, constraintWidget8.mListAnchors[i11].mSolverVariable, constraintWidget8.mListAnchors[i11].getMargin(), 6);
                }
                arrayList = chainHead2.mWeightedMatchConstraintsWidgets;
                if (arrayList != null) {
                    int size = arrayList.size();
                    if (size > 1) {
                        float f5 = (!chainHead2.mHasUndefinedWeights || chainHead2.mHasComplexMatchWeights) ? f4 : (float) chainHead2.mWidgetsMatchCount;
                        float f6 = 0.0f;
                        float f7 = 0.0f;
                        ConstraintWidget constraintWidget17 = null;
                        int i12 = 0;
                        while (i12 < size) {
                            ConstraintWidget constraintWidget18 = (ConstraintWidget) arrayList.get(i12);
                            float f8 = constraintWidget18.mWeight[i];
                            if (f8 < f6) {
                                if (chainHead2.mHasComplexMatchWeights) {
                                    linearSystem2.addEquality(constraintWidget18.mListAnchors[i2 + 1].mSolverVariable, constraintWidget18.mListAnchors[i2].mSolverVariable, 0, 4);
                                    arrayList2 = arrayList;
                                    i5 = size;
                                    i12++;
                                    size = i5;
                                    arrayList = arrayList2;
                                    f6 = 0.0f;
                                } else {
                                    f8 = 1.0f;
                                    f6 = 0.0f;
                                }
                            }
                            if (f8 == f6) {
                                linearSystem2.addEquality(constraintWidget18.mListAnchors[i2 + 1].mSolverVariable, constraintWidget18.mListAnchors[i2].mSolverVariable, 0, 6);
                                arrayList2 = arrayList;
                                i5 = size;
                                i12++;
                                size = i5;
                                arrayList = arrayList2;
                                f6 = 0.0f;
                            } else {
                                if (constraintWidget17 != null) {
                                    SolverVariable solverVariable7 = constraintWidget17.mListAnchors[i2].mSolverVariable;
                                    int i13 = i2 + 1;
                                    SolverVariable solverVariable8 = constraintWidget17.mListAnchors[i13].mSolverVariable;
                                    SolverVariable solverVariable9 = constraintWidget18.mListAnchors[i2].mSolverVariable;
                                    arrayList2 = arrayList;
                                    SolverVariable solverVariable10 = constraintWidget18.mListAnchors[i13].mSolverVariable;
                                    i5 = size;
                                    ArrayRow createRow = linearSystem.createRow();
                                    createRow.createRowEqualMatchDimensions(f7, f5, f8, solverVariable7, solverVariable8, solverVariable9, solverVariable10);
                                    linearSystem2.addConstraint(createRow);
                                } else {
                                    arrayList2 = arrayList;
                                    i5 = size;
                                }
                                f7 = f8;
                                constraintWidget17 = constraintWidget18;
                                i12++;
                                size = i5;
                                arrayList = arrayList2;
                                f6 = 0.0f;
                            }
                        }
                    }
                }
                if (constraintWidget9 == null && (constraintWidget9 == constraintWidget10 || z7)) {
                    ConstraintAnchor constraintAnchor9 = constraintWidget7.mListAnchors[i2];
                    int i14 = i2 + 1;
                    ConstraintAnchor constraintAnchor10 = constraintWidget8.mListAnchors[i14];
                    SolverVariable solverVariable11 = constraintWidget7.mListAnchors[i2].mTarget != null ? constraintWidget7.mListAnchors[i2].mTarget.mSolverVariable : null;
                    SolverVariable solverVariable12 = constraintWidget8.mListAnchors[i14].mTarget != null ? constraintWidget8.mListAnchors[i14].mTarget.mSolverVariable : null;
                    if (constraintWidget9 == constraintWidget10) {
                        constraintAnchor9 = constraintWidget9.mListAnchors[i2];
                        constraintAnchor10 = constraintWidget9.mListAnchors[i14];
                    }
                    if (!(solverVariable11 == null || solverVariable12 == null)) {
                        if (i == 0) {
                            f = constraintWidget16.mHorizontalBiasPercent;
                        } else {
                            f = constraintWidget16.mVerticalBiasPercent;
                        }
                        linearSystem.addCentering(constraintAnchor9.mSolverVariable, solverVariable11, constraintAnchor9.getMargin(), f, solverVariable12, constraintAnchor10.mSolverVariable, constraintAnchor10.getMargin(), 5);
                    }
                } else if (z11 || constraintWidget9 == null) {
                    int i15 = 8;
                    if (z12 && constraintWidget9 != null) {
                        boolean z13 = chainHead2.mWidgetsMatchCount <= 0 && chainHead2.mWidgetsCount == chainHead2.mWidgetsMatchCount;
                        constraintWidget2 = constraintWidget9;
                        ConstraintWidget constraintWidget19 = constraintWidget2;
                        while (constraintWidget2 != null) {
                            ConstraintWidget constraintWidget20 = constraintWidget2.mNextChainWidget[i];
                            while (constraintWidget20 != null && constraintWidget20.getVisibility() == i15) {
                                constraintWidget20 = constraintWidget20.mNextChainWidget[i];
                            }
                            if (constraintWidget2 == constraintWidget9 || constraintWidget2 == constraintWidget10 || constraintWidget20 == null) {
                                constraintWidget3 = constraintWidget19;
                                i4 = i15;
                            } else {
                                ConstraintWidget constraintWidget21 = constraintWidget20 == constraintWidget10 ? null : constraintWidget20;
                                ConstraintAnchor constraintAnchor11 = constraintWidget2.mListAnchors[i2];
                                SolverVariable solverVariable13 = constraintAnchor11.mSolverVariable;
                                if (constraintAnchor11.mTarget != null) {
                                    SolverVariable solverVariable14 = constraintAnchor11.mTarget.mSolverVariable;
                                }
                                int i16 = i2 + 1;
                                SolverVariable solverVariable15 = constraintWidget19.mListAnchors[i16].mSolverVariable;
                                int margin2 = constraintAnchor11.getMargin();
                                int margin3 = constraintWidget2.mListAnchors[i16].getMargin();
                                if (constraintWidget21 != null) {
                                    constraintAnchor4 = constraintWidget21.mListAnchors[i2];
                                    solverVariable4 = constraintAnchor4.mSolverVariable;
                                    solverVariable3 = constraintAnchor4.mTarget != null ? constraintAnchor4.mTarget.mSolverVariable : null;
                                } else {
                                    constraintAnchor4 = constraintWidget2.mListAnchors[i16].mTarget;
                                    solverVariable4 = constraintAnchor4 != null ? constraintAnchor4.mSolverVariable : null;
                                    solverVariable3 = constraintWidget2.mListAnchors[i16].mSolverVariable;
                                }
                                if (constraintAnchor4 != null) {
                                    margin3 += constraintAnchor4.getMargin();
                                }
                                int i17 = margin3;
                                if (constraintWidget19 != null) {
                                    margin2 += constraintWidget19.mListAnchors[i16].getMargin();
                                }
                                int i18 = margin2;
                                int i19 = z13 ? 6 : 4;
                                if (solverVariable13 == null || solverVariable15 == null || solverVariable4 == null || solverVariable3 == null) {
                                    constraintWidget4 = constraintWidget21;
                                    constraintWidget3 = constraintWidget19;
                                    i4 = 8;
                                } else {
                                    constraintWidget4 = constraintWidget21;
                                    int i20 = i17;
                                    constraintWidget3 = constraintWidget19;
                                    i4 = 8;
                                    linearSystem.addCentering(solverVariable13, solverVariable15, i18, 0.5f, solverVariable4, solverVariable3, i20, i19);
                                }
                                constraintWidget20 = constraintWidget4;
                            }
                            if (constraintWidget2.getVisibility() == i4) {
                                constraintWidget2 = constraintWidget3;
                            }
                            i15 = i4;
                            constraintWidget19 = constraintWidget2;
                            constraintWidget2 = constraintWidget20;
                        }
                        ConstraintAnchor constraintAnchor12 = constraintWidget9.mListAnchors[i2];
                        constraintAnchor = constraintWidget7.mListAnchors[i2].mTarget;
                        int i21 = i2 + 1;
                        constraintAnchor2 = constraintWidget10.mListAnchors[i21];
                        constraintAnchor3 = constraintWidget8.mListAnchors[i21].mTarget;
                        if (constraintAnchor != null) {
                            i3 = 5;
                        } else if (constraintWidget9 != constraintWidget10) {
                            i3 = 5;
                            linearSystem2.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 5);
                        } else {
                            i3 = 5;
                            if (constraintAnchor3 != null) {
                                linearSystem.addCentering(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 0.5f, constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, constraintAnchor2.getMargin(), 5);
                            }
                        }
                        if (!(constraintAnchor3 == null || constraintWidget9 == constraintWidget10)) {
                            linearSystem2.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
                        }
                    }
                } else {
                    boolean z14 = chainHead2.mWidgetsMatchCount > 0 && chainHead2.mWidgetsCount == chainHead2.mWidgetsMatchCount;
                    ConstraintWidget constraintWidget22 = constraintWidget9;
                    ConstraintWidget constraintWidget23 = constraintWidget22;
                    while (constraintWidget22 != null) {
                        ConstraintWidget constraintWidget24 = constraintWidget22.mNextChainWidget[i];
                        while (true) {
                            if (constraintWidget24 != null) {
                                if (constraintWidget24.getVisibility() != 8) {
                                    break;
                                }
                                constraintWidget24 = constraintWidget24.mNextChainWidget[i];
                            } else {
                                break;
                            }
                        }
                        if (constraintWidget24 != null || constraintWidget22 == constraintWidget10) {
                            ConstraintAnchor constraintAnchor13 = constraintWidget22.mListAnchors[i2];
                            SolverVariable solverVariable16 = constraintAnchor13.mSolverVariable;
                            SolverVariable solverVariable17 = constraintAnchor13.mTarget != null ? constraintAnchor13.mTarget.mSolverVariable : null;
                            if (constraintWidget23 != constraintWidget22) {
                                solverVariable17 = constraintWidget23.mListAnchors[i2 + 1].mSolverVariable;
                            } else if (constraintWidget22 == constraintWidget9 && constraintWidget23 == constraintWidget22) {
                                solverVariable17 = constraintWidget7.mListAnchors[i2].mTarget != null ? constraintWidget7.mListAnchors[i2].mTarget.mSolverVariable : null;
                            }
                            int margin4 = constraintAnchor13.getMargin();
                            int i22 = i2 + 1;
                            int margin5 = constraintWidget22.mListAnchors[i22].getMargin();
                            if (constraintWidget24 != null) {
                                constraintAnchor5 = constraintWidget24.mListAnchors[i2];
                                SolverVariable solverVariable18 = constraintAnchor5.mSolverVariable;
                                solverVariable5 = constraintWidget22.mListAnchors[i22].mSolverVariable;
                                solverVariable6 = solverVariable18;
                            } else {
                                constraintAnchor5 = constraintWidget8.mListAnchors[i22].mTarget;
                                solverVariable6 = constraintAnchor5 != null ? constraintAnchor5.mSolverVariable : null;
                                solverVariable5 = constraintWidget22.mListAnchors[i22].mSolverVariable;
                            }
                            if (constraintAnchor5 != null) {
                                margin5 += constraintAnchor5.getMargin();
                            }
                            if (constraintWidget23 != null) {
                                margin4 += constraintWidget23.mListAnchors[i22].getMargin();
                            }
                            if (!(solverVariable16 == null || solverVariable17 == null || solverVariable6 == null || solverVariable5 == null)) {
                                if (constraintWidget22 == constraintWidget9) {
                                    margin4 = constraintWidget9.mListAnchors[i2].getMargin();
                                }
                                int i23 = margin4;
                                int margin6 = constraintWidget22 == constraintWidget10 ? constraintWidget10.mListAnchors[i22].getMargin() : margin5;
                                int i24 = i23;
                                SolverVariable solverVariable19 = solverVariable6;
                                SolverVariable solverVariable20 = solverVariable5;
                                int i25 = margin6;
                                constraintWidget5 = constraintWidget24;
                                linearSystem.addCentering(solverVariable16, solverVariable17, i24, 0.5f, solverVariable19, solverVariable20, i25, z14 ? 6 : 4);
                                if (constraintWidget22.getVisibility() == 8) {
                                    constraintWidget23 = constraintWidget22;
                                }
                                constraintWidget22 = constraintWidget5;
                            }
                        }
                        constraintWidget5 = constraintWidget24;
                        if (constraintWidget22.getVisibility() == 8) {
                        }
                        constraintWidget22 = constraintWidget5;
                    }
                }
                if ((!z11 || z12) && constraintWidget9 != null) {
                    ConstraintAnchor constraintAnchor14 = constraintWidget9.mListAnchors[i2];
                    int i26 = i2 + 1;
                    ConstraintAnchor constraintAnchor15 = constraintWidget10.mListAnchors[i26];
                    solverVariable = constraintAnchor14.mTarget == null ? constraintAnchor14.mTarget.mSolverVariable : null;
                    ? r4 = constraintAnchor15.mTarget == null ? constraintAnchor15.mTarget.mSolverVariable : 0;
                    if (constraintWidget8 == constraintWidget10) {
                        ConstraintAnchor constraintAnchor16 = constraintWidget8.mListAnchors[i26];
                        if (constraintAnchor16.mTarget != null) {
                            constraintWidget = constraintAnchor16.mTarget.mSolverVariable;
                        }
                        solverVariable2 = constraintWidget;
                    } else {
                        solverVariable2 = r4;
                    }
                    if (constraintWidget9 == constraintWidget10) {
                        constraintAnchor14 = constraintWidget9.mListAnchors[i2];
                        constraintAnchor15 = constraintWidget9.mListAnchors[i26];
                    }
                    if (solverVariable != null && solverVariable2 != 0) {
                        int margin7 = constraintAnchor14.getMargin();
                        if (constraintWidget10 != null) {
                            constraintWidget8 = constraintWidget10;
                        }
                        linearSystem.addCentering(constraintAnchor14.mSolverVariable, solverVariable, margin7, 0.5f, solverVariable2, constraintAnchor15.mSolverVariable, constraintWidget8.mListAnchors[i26].getMargin(), 5);
                        return;
                    }
                }
                return;
            }
        }
        if (z6) {
        }
        arrayList = chainHead2.mWeightedMatchConstraintsWidgets;
        if (arrayList != null) {
        }
        if (constraintWidget9 == null) {
        }
        if (z11) {
        }
        int i152 = 8;
        if (chainHead2.mWidgetsMatchCount <= 0) {
        }
        constraintWidget2 = constraintWidget9;
        ConstraintWidget constraintWidget192 = constraintWidget2;
        while (constraintWidget2 != null) {
        }
        ConstraintAnchor constraintAnchor122 = constraintWidget9.mListAnchors[i2];
        constraintAnchor = constraintWidget7.mListAnchors[i2].mTarget;
        int i212 = i2 + 1;
        constraintAnchor2 = constraintWidget10.mListAnchors[i212];
        constraintAnchor3 = constraintWidget8.mListAnchors[i212].mTarget;
        if (constraintAnchor != null) {
        }
        linearSystem2.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
        if (!z11) {
        }
        ConstraintAnchor constraintAnchor142 = constraintWidget9.mListAnchors[i2];
        int i262 = i2 + 1;
        ConstraintAnchor constraintAnchor152 = constraintWidget10.mListAnchors[i262];
        if (constraintAnchor142.mTarget == null) {
        }
        if (constraintAnchor152.mTarget == null) {
        }
        if (constraintWidget8 == constraintWidget10) {
        }
        if (constraintWidget9 == constraintWidget10) {
        }
        if (solverVariable != null) {
        }
    }
}
