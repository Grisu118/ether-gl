/*
 * Copyright (c) 2013 - 2015 Stefan Muller Arisona, Simon Schubiger, Samuel von Stachelski
 * Copyright (c) 2013 - 2015 FHNW & ETH Zurich
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

package ch.fhnw.util.math;

public final class Transform {

	// M = T * RX * RY * RZ * S
	public static Mat4 trs(float tx, float ty, float tz, float rx, float ry, float rz, float sx, float sy, float sz) {
		return Mat4.multiply(Mat4.translate(tx, ty, tz), Mat4.rotate(rx, Vec3.X), Mat4.rotate(ry, Vec3.Y), Mat4.rotate(rz, Vec3.Z), Mat4.scale(sx, sy, sz));
	}

	// M = T * RX * RY * RZ * S
	public static Mat4 trs(Vec3 t, Vec3 r, Vec3 s) {
		return trs(t.x, t.y, t.z, r.x, r.y, r.z, s.x, s.y, s.z);
	}
	
/*

	public float[] transformVertices(float[] vertices) {
		validateVertexTransform(origin);
		return vertexTransform.transform(vertices);
	}

	public float[] transformNormals(float[] normals) {
		validateNormalTransform();
		return normalTransform.transform(normals);
	}

	private void validateVertexTransform(Vec3 origin) {
		if (vertexTransform == null) {
			vertexTransform = Mat4.multiply(Mat4.translate(translation), Mat4.rotate(rotation.x, Vec3.X), Mat4.rotate(rotation.y, Vec3.Y),
					Mat4.rotate(rotation.z, Vec3.Z), Mat4.scale(scale), Mat4.translate(origin.negate()));
		}
	}

	private void validateNormalTransform() {
		if (normalTransform == null) {
			normalTransform = Mat3.multiply(Mat3.rotate(rotation.x, Vec3.X), Mat3.rotate(rotation.y, Vec3.Y), Mat3.rotate(rotation.z, Vec3.Z),
					Mat3.scale(scale));
			normalTransform = normalTransform.inverse().transpose();
		}
	}
*/
}
