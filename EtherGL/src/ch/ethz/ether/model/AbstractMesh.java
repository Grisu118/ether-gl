package ch.ethz.ether.model;

import ch.ethz.ether.geom.BoundingVolume;
import ch.ethz.ether.geom.Vec3;
import ch.ethz.util.IAddOnlyFloatList;

import java.util.Map;

/**
 * Created by radar on 05/12/13.
 */
public abstract class AbstractMesh implements IMesh {
    private class TransformCache {
        TransformCache() {
            triangleVertices = transform.transformVertices(AbstractMesh.this.triangleVertices);
            triangleNormals = transform.transformNormals(AbstractMesh.this.triangleNormals);
            edgeVertices = transform.transformVertices(AbstractMesh.this.edgeVertices);
            pointVertices = transform.transformVertices(AbstractMesh.this.pointVertices);
        }

        final float[] triangleVertices;
        final float[] triangleNormals;
        final float[] edgeVertices;
        final float[] pointVertices;
    }

    private BoundingVolume bounds;
    private Transform transform = new Transform();
    private float[] triangleVertices;
    private float[] triangleNormals;
    private float[] triangleColors;
    private float[] edgeVertices;
    private float[] edgeColors;
    private float[] pointVertices;
    private float[] pointColors;

    private TransformCache cache;

    protected AbstractMesh() {
        this(Vec3.ZERO);
    }

    protected AbstractMesh(Vec3 origin) {
        transform.setOrigin(origin);
    }

    protected void setTriangles(float[] vertices, float[] normals, float[] colors) {
        triangleVertices = vertices;
        triangleNormals = normals;
        triangleColors = colors;
        invalidateCache();
    }

    protected void setEdges(float[] vertices, float[] colors) {
        edgeVertices = vertices;
        edgeColors = colors;
        invalidateCache();
    }

    protected void setPoints(float[] vertices, float[] colors) {
        pointVertices = vertices;
        pointColors = colors;
        invalidateCache();
    }

    @Override
    public BoundingVolume getBounds() {
        validateCache();
        return bounds;
    }

    @Override
    public Vec3 getOrigin() {
        return transform.getOrigin();
    }

    public void setOrigin(Vec3 origin) {
        transform.setOrigin(origin);
        invalidateCache();
    }

    @Override
    public Vec3 getTranslation() {
        return transform.getTranslation();
    }

    @Override
    public void setTranslation(Vec3 translation) {
        transform.setTranslation(translation);
        invalidateCache();
    }

    @Override
    public Vec3 getRotation() {
        return transform.getRotation();
    }

    @Override
    public void setRotation(Vec3 rotation) {
        transform.setRotation(rotation);
        invalidateCache();
    }

    @Override
    public Vec3 getScale() {
        return transform.getScale();
    }

    @Override
    public void setScale(Vec3 scale) {
        transform.setScale(scale);
        invalidateCache();
    }

    @Override
    public boolean pick(int x, int y, float[] viewMatrix, float[] projMatrix, Map<Float, IGeometry> geometries) {
        return false;
    }

    @Override
    public boolean getTriangleVertices(IAddOnlyFloatList dst) {
        validateCache();
        return dst.addAll(cache.triangleVertices);
    }

    @Override
    public boolean getTriangleNormals(IAddOnlyFloatList dst) {
        validateCache();
        return dst.addAll(cache.triangleNormals);
    }

    @Override
    public boolean getTriangleColors(IAddOnlyFloatList dst) {
        return dst.addAll(triangleColors);
    }

    @Override
    public boolean getEdgeVertices(IAddOnlyFloatList dst) {
        validateCache();
        return dst.addAll(cache.edgeVertices);
    }

    @Override
    public boolean getEdgeColors(IAddOnlyFloatList dst) {
        return dst.addAll(edgeColors);
    }

    @Override
    public boolean getPointVertices(IAddOnlyFloatList dst) {
        validateCache();
        return dst.addAll(cache.pointVertices);
    }

    @Override
    public boolean getPointColors(IAddOnlyFloatList dst) {
        return dst.addAll(pointColors);
    }

    private void invalidateCache() {
        cache = null;
        bounds = null;
    }

    private void validateCache() {
        if (cache == null) {
            cache = new TransformCache();
            bounds = new BoundingVolume();
            bounds.add(cache.triangleVertices);
            bounds.add(cache.edgeVertices);
            bounds.add(cache.pointVertices);
        }
    }
}
