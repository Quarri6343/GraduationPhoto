package quarri6343.graduationphoto;

import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.opengl.GL11;

public final class ProjectionUtility {
    
    private static Matrix4f viewProjectionMatrix;
    
    public static Vector3f worldToViewportPosition(Vector3d posIn) {
        final Vector4f vector4f = new Vector4f((float)posIn.x, (float)posIn.y, (float)posIn.z, 1f);

        vector4f.transform(viewProjectionMatrix);

        final float invert = 1f / vector4f.w();
        float x = vector4f.x();
        float y = vector4f.y();
        vector4f.setX(x * invert);
        vector4f.setY(y * invert);

        return new Vector3f(vector4f.x(), vector4f.y(), vector4f.z());
    }

    public static void updateViewProjectionMatrix() {
        float[] modelview = new float[16];
        float[] projection = new float[16];
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projection);

        Matrix4f modelMatrix = new Matrix4f(modelview);
        Matrix4f projectionMatrix = new Matrix4f(projection);

        modelMatrix.multiply(projectionMatrix);
        viewProjectionMatrix =  modelMatrix;
    }
}
