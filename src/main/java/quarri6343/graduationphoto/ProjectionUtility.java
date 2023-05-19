package quarri6343.graduationphoto;

import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

public final class ProjectionUtility {

    private static Matrix4f viewModelMatrix;
    private static Matrix4f projectionMatrix;

    public static Vector3f worldToViewportPosition(Matrix4f viewModelMatrix, Vector3d posIn) {
        final Vector4f vector4f = new Vector4f((float) posIn.x / 3f, (float) posIn.y / 3f, (float) posIn.z / 3f, 1f);

        vector4f.transform(viewModelMatrix);
        vector4f.transform(projectionMatrix);

        final float invert = 1f / vector4f.w();

        return new Vector3f(vector4f.x() * invert, vector4f.y() * invert, vector4f.z() * invert);
    }

    public static void updateViewProjectionMatrix(Matrix4f viewModelMatrix, Matrix4f projectionMatrix) {
        ProjectionUtility.viewModelMatrix = viewModelMatrix;
        ProjectionUtility.projectionMatrix = projectionMatrix;
    }
}
