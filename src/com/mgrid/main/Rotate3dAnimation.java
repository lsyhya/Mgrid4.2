package com.mgrid.main;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * 从两个角度以 Y 轴为基准做翻滚动画。
 * 同时提供 Z 轴的转换（深度）动画以提升效果。
 */
public class Rotate3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    /**
     * 建立一个据 Y 轴翻滚的 3D 动画效果。 需要定义滚动的开始角度和结束角度。
     * 使用标准角度值定义角度大小， 在 2D 空间内以中心点执行翻滚动作。
     * 以 X 和 Y 定义这个中心点， 对应的函数参数为  centerX 和 centerY。
     * 当动画开始时， 将执行一个 Z 轴 （深度） 的转换，传 depthZ 值设定这个翻转深度。
     * 指定 reverse 参数来设定翻滚后的图像是否需要反转(如果显示的)。
     *
     * @param fromDegrees 3D 旋转的开始角度
     * @param toDegrees 3D 旋转的结束角度
     * @param centerX 3D 旋转的 X 轴中心点
     * @param centerY 3D 旋转的 Y 轴中心点
     * @param reverse true 画面需要反转
     */
    public Rotate3dAnimation(float fromDegrees, float toDegrees,
            float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
