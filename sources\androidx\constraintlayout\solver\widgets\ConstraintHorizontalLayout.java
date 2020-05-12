package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor.Strength;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer {
    private ContentAlignment mAlignment = ContentAlignment.MIDDLE;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public ConstraintHorizontalLayout() {
    }

    public ConstraintHorizontalLayout(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
    }

    public ConstraintHorizontalLayout(int i, int i2) {
        super(i, i2);
    }

    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    public void addToSolver(LinearSystem linearSystem) {
        if (this.mChildren.size() != 0) {
            int i = 0;
            int size = this.mChildren.size();
            ConstraintHorizontalLayout constraintHorizontalLayout = this;
            while (i < size) {
                ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
                if (constraintHorizontalLayout != this) {
                    constraintWidget.connect(Type.LEFT, (ConstraintWidget) constraintHorizontalLayout, Type.RIGHT);
                    constraintHorizontalLayout.connect(Type.RIGHT, constraintWidget, Type.LEFT);
                } else {
                    Strength strength = Strength.STRONG;
                    if (this.mAlignment == ContentAlignment.END) {
                        strength = Strength.WEAK;
                    }
                    Strength strength2 = strength;
                    constraintWidget.connect(Type.LEFT, (ConstraintWidget) constraintHorizontalLayout, Type.LEFT, 0, strength2);
                }
                constraintWidget.connect(Type.TOP, (ConstraintWidget) this, Type.TOP);
                constraintWidget.connect(Type.BOTTOM, (ConstraintWidget) this, Type.BOTTOM);
                i++;
                constraintHorizontalLayout = constraintWidget;
            }
            if (constraintHorizontalLayout != this) {
                Strength strength3 = Strength.STRONG;
                if (this.mAlignment == ContentAlignment.BEGIN) {
                    strength3 = Strength.WEAK;
                }
                ConstraintHorizontalLayout constraintHorizontalLayout2 = constraintHorizontalLayout;
                constraintHorizontalLayout2.connect(Type.RIGHT, (ConstraintWidget) this, Type.RIGHT, 0, strength3);
            }
        }
        super.addToSolver(linearSystem);
    }
}
