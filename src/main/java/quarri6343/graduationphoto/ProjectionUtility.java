package quarri6343.graduationphoto;

import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector4f;

import java.nio.IntBuffer;

public final class ProjectionUtility {

    private static Matrix4f viewModelMatrix;
    private static Matrix4f projectionMatrix;

    public static Vector3 worldToScreenPosition(Matrix4f viewModelMatrix, Vector3 posIn, IntBuffer viewport) {
        if(projectionMatrix == null)
            return Vector3.ZERO;
//        final Vector4f vector4f = new Vector4f((float) posIn.x, (float) posIn.y, (float) posIn.z, 1f);
//
//        vector4f.transform(viewModelMatrix);
//        vector4f.transform(projectionMatrix);
//
//        final float invert = 1f / vector4f.w();
//        return new Vector3f(vector4f.x() * invert, vector4f.y() * invert, vector4f.z() * invert);
        return  gluProject(posIn, new Matrix4(viewModelMatrix), new Matrix4(projectionMatrix), viewport);
    }

    public static void updateViewProjectionMatrix(Matrix4f viewModelMatrix, Matrix4f projectionMatrix) {
        ProjectionUtility.viewModelMatrix = viewModelMatrix;
        ProjectionUtility.projectionMatrix = projectionMatrix;
    }
    
    private static Vector3 gluProject(Vector3 obj, Matrix4 modelMatrix, Matrix4 projMatrix, IntBuffer viewport) {
        Vector4f o = new Vector4f((float) obj.x, (float) obj.y, (float) obj.z, 1.0F);
        multMatrix(modelMatrix, o);
        multMatrix(projMatrix, o);

        if (o.w() == 0) {
            return Vector3.ZERO.copy();
        }
        o.setW((1.0F / o.w()) * 0.5F);

        o.setX(o.x() * o.w() + 0.5F);
        o.setY(o.y() * o.w() + 0.5F);
        o.setZ(o.z() * o.w() + 0.5F);

        Vector3 winPos = new Vector3();
        winPos.z = o.z();

        winPos.x = o.x() * viewport.get(viewport.position() + 2) + viewport.get(viewport.position() + 0);
        winPos.y = o.y() * viewport.get(viewport.position() + 3) + viewport.get(viewport.position() + 1);
        return winPos;
    }

    private static void multMatrix(Matrix4 mat, Vector4f vec) {
        double x = mat.m00 * vec.x() + mat.m01 * vec.y() + mat.m02 * vec.z() + mat.m03 * vec.w();
        double y = mat.m10 * vec.x() + mat.m11 * vec.y() + mat.m12 * vec.z() + mat.m13 * vec.w();
        double z = mat.m20 * vec.x() + mat.m21 * vec.y() + mat.m22 * vec.z() + mat.m23 * vec.w();
        double w = mat.m30 * vec.x() + mat.m31 * vec.y() + mat.m32 * vec.z() + mat.m33 * vec.w();
        vec.set((float) x, (float) y, (float) z, (float) w);
    }
}
