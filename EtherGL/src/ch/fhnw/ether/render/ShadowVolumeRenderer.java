/*
 * Copyright (c) 2013 - 2014 FHNW & ETH Zurich (Stefan Muller Arisona & Simon Schubiger)
 * Copyright (c) 2013 - 2014 Stefan Muller Arisona & Simon Schubiger
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *  Neither the name of FHNW / ETH Zurich nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.fhnw.ether.render;


/**
 * Very basic shadow volume renderer.
 *
 * @author radar
 */
// XXX work in progress (silhouettes, shader optimizations, etc...)

public class ShadowVolumeRenderer /*implements IRenderer*/ {
/*
    private enum StencilShadowMethod {
		ZPASS,
		ZFAIL
	}
	
	private static final StencilShadowMethod STENCIL_SHADOW_METHOD = StencilShadowMethod.ZFAIL;
	
	// XXX work in progress
	private static final float[] MODEL_COLOR = { 1.0f, 1.0f, 1.0f, 1.0f };
	private static final float[] SHADOW_COLOR = { 1, 0, 0, 1 };
	
	private static final float[] LIGHT_POSITION = { 10, 10, 10 };
	
	private boolean enableShadows = false;

	@Override
	public void renderModel(GL2 gl, IModel model, IView view) {
		BoundingBox bounds = model.getBounds();
		IRenderGroup group = model.getRenderGroups().get(0);
			
		// enable depth test
		gl.glEnable(GL2.GL_DEPTH_TEST);

		// render ground plane (XXX FIXME: currently too small...)
		gl.glColor4fv(NavigationGrid.GRID_COLOR, 0);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3d(bounds.getMinX(), bounds.getMinY(), -0.001);
		gl.glVertex3d(bounds.getMaxX(), bounds.getMinY(), -0.001);
		gl.glVertex3d(bounds.getMaxX(), bounds.getMaxY(), -0.001);
		gl.glVertex3d(bounds.getMinX(), bounds.getMaxY(), -0.001);
		gl.glEnd();

		// render geometry
		renderGeometry(gl, group);
		
		//debug
		//gl.glColor4d(1.0, 1.0, 0.0, 0.5);
		//renderShadowVolumes(gl, view, true);

		// render shadow volumes
		if (enableShadows) renderShadows(gl, group, LIGHT_POSITION, SHADOW_COLOR);
		
		// cleanup
		gl.glDisable(GL2.GL_DEPTH_TEST);
	}
	
	public boolean getEnableShadows() {
		return enableShadows;
	}
	
	public void setEnableShadows(boolean enableShadows) {
		this.enableShadows = enableShadows;
	}

	private void renderGeometry(GL2 gl, IRenderGroup group) {
		gl.glColor3fv(MODEL_COLOR, 0);
		drawTriangles(gl, group.getFaces(), group.getNormals(), group.getColors());
	}

	private void renderShadows(GL2 gl, IRenderGroup group, float[] lightPosition, float[] shadowColor) {
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);
		gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
		gl.glPolygonOffset(0.0f, 100.0f);

		gl.glEnable(GL.GL_STENCIL_TEST);
		gl.glEnable(GL2.GL_STENCIL_TEST_TWO_SIDE_EXT);			
		if (STENCIL_SHADOW_METHOD == StencilShadowMethod.ZFAIL) {
			// z-fail
			gl.glActiveStencilFaceEXT(GL.GL_BACK);
			gl.glStencilFunc(GL.GL_ALWAYS, 0, 0xff);
			gl.glStencilOp(GL.GL_KEEP, GL.GL_INCR_WRAP, GL.GL_KEEP);
	
			gl.glActiveStencilFaceEXT(GL.GL_FRONT);
			gl.glStencilFunc(GL.GL_ALWAYS, 0, 0xff);
			gl.glStencilOp(GL.GL_KEEP, GL.GL_DECR_WRAP, GL.GL_KEEP);

			renderShadowVolumes(gl, group, lightPosition, true);
		} else {
			// z-pass
			gl.glActiveStencilFaceEXT(GL.GL_BACK);
			gl.glStencilFunc(GL.GL_ALWAYS, 0, 0xff);
			gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_INCR_WRAP);
	
			gl.glActiveStencilFaceEXT(GL.GL_FRONT);
			gl.glStencilFunc(GL.GL_ALWAYS, 0, 0xff);
			gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_DECR_WRAP);

			renderShadowVolumes(gl, group, lightPosition, false);
		}
		gl.glDisable(GL2.GL_STENCIL_TEST_TWO_SIDE_EXT);				
		gl.glDisable(GL.GL_STENCIL_TEST);

		gl.glDisable(GL.GL_POLYGON_OFFSET_FILL);
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);

		gl.glEnable(GL.GL_STENCIL_TEST);
		gl.glStencilFunc(GL.GL_NOTEQUAL, 0x0, 0xff);
		gl.glStencilOp(GL.GL_REPLACE, GL.GL_REPLACE, GL.GL_REPLACE);
		renderShadowOverlay(gl, shadowColor);
		gl.glDisable(GL.GL_STENCIL_TEST);
	}


	private final float[] _f = new float[9]; // current face (triangle)
	private final float[] _e = new float[9]; // current extruded face

	private void renderShadowVolumes(GL2 gl, IRenderGroup group, float[] lightPosition, boolean drawCaps) {
		float[] vertices = group.getFaces();
		float[] normals = group.getNormals();
		
		for (int i = 0; i < vertices.length; i+=9) {
			if (isFacingLight(vertices, normals, i, lightPosition)) {
				_f[0] = vertices[i+0];
				_f[1] = vertices[i+1];
				_f[2] = vertices[i+2];
				_f[3] = vertices[i+3];
				_f[4] = vertices[i+4];
				_f[5] = vertices[i+5];
				_f[6] = vertices[i+6];
				_f[7] = vertices[i+7];
				_f[8] = vertices[i+8];
			} else {
				_f[0] = vertices[i+6];
				_f[1] = vertices[i+7];
				_f[2] = vertices[i+8];
				_f[3] = vertices[i+3];
				_f[4] = vertices[i+4];
				_f[5] = vertices[i+5];
				_f[6] = vertices[i+0];
				_f[7] = vertices[i+1];
				_f[8] = vertices[i+2];
			}
			
			_e[0] = _f[0] - lightPosition[0];
			_e[1] = _f[1] - lightPosition[1];
			_e[2] = _f[2] - lightPosition[2];
			_e[3] = _f[3] - lightPosition[0];
			_e[4] = _f[4] - lightPosition[1];
			_e[5] = _f[5] - lightPosition[2];
			_e[6] = _f[6] - lightPosition[0];
			_e[7] = _f[7] - lightPosition[1];
			_e[8] = _f[8] - lightPosition[2];
			
			// front cap & back cap
			if (drawCaps) {
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex3f(_f[0], _f[1], _f[2]);
				gl.glVertex3f(_f[3], _f[4], _f[5]);
				gl.glVertex3f(_f[6], _f[7], _f[8]);
				gl.glVertex4f(_e[6], _e[7], _e[8], 0);
				gl.glVertex4f(_e[3], _e[4], _e[5], 0);
				gl.glVertex4f(_e[0], _e[1], _e[2], 0);
				gl.glEnd();
			}
			
			// sides
			gl.glBegin(GL2.GL_QUADS);
			gl.glVertex3f(_f[0], _f[1], _f[2]);
			gl.glVertex4f(_e[0], _e[1], _e[2], 0);
			gl.glVertex4f(_e[3], _e[4], _e[5], 0);
			gl.glVertex3f(_f[3], _f[4], _f[5]);

			gl.glVertex3f(_f[3], _f[4], _f[5]);
			gl.glVertex4f(_e[3], _e[4], _e[5], 0);
			gl.glVertex4f(_e[6], _e[7], _e[8], 0);
			gl.glVertex3f(_f[6], _f[7], _f[8]);
			
			gl.glVertex3f(_f[6], _f[7], _f[8]);
			gl.glVertex4f(_e[6], _e[7], _e[8], 0);
			gl.glVertex4f(_e[0], _e[1], _e[2], 0);
			gl.glVertex3f(_f[0], _f[1], _f[2]);
			gl.glEnd();
		}
	}
	
	
	private void renderShadowOverlay(GL2 gl, float[] shadowColor) {
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, 1, 1, 0, 0, 1);
		gl.glDisable(GL.GL_DEPTH_TEST);

		gl.glColor4fv(shadowColor, 0);
		gl.glRectd(0, 0, 1, 1);

		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPopMatrix();
	}
	
	private static void drawTriangles(GL2 gl, float[] vertices, float[] normals, float[] colors) {
		if (colors == null) {
			gl.glColor4f(1f, 1f, 1f, 1f);
		}
		gl.glBegin(GL2.GL_TRIANGLES);
		for (int i = 0; i < vertices.length; i+=3) {
			if (colors != null) gl.glColor3fv(colors, i);
			if (normals != null) gl.glNormal3fv(normals, i);
			gl.glVertex3fv(vertices, i);
		}			
		gl.glEnd();		
	}

	private static boolean isFacingLight(float v[], float n[], int i, float[] light) {
		return ((light[0] - v[i+0]) * n[i+0] + (light[1] - v[i+1]) * n[i+1] + (light[2] - v[i+2]) * n[i+2]) < 0;
	}
*/
}
