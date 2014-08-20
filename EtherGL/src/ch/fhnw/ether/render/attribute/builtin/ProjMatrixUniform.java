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

package ch.fhnw.ether.render.attribute.builtin;

import java.util.function.Supplier;

import ch.fhnw.ether.geom.Mat4;
import ch.fhnw.ether.render.attribute.Mat4FloatUniformAttribute;

public final class ProjMatrixUniform extends Mat4FloatUniformAttribute {
	public static final String ID = "builtin.proj_matrix";
	private static final String DEFAULT_SHADER_NAME = "projMatrix";

	public ProjMatrixUniform() {
		super(ID, DEFAULT_SHADER_NAME);
	}

	public ProjMatrixUniform(String shaderName) {
		super(ID, shaderName);
	}
	
	public ProjMatrixUniform(Supplier<Mat4> supplier) {
		super(ID, DEFAULT_SHADER_NAME, supplier);
	}

	public ProjMatrixUniform(String shaderName, Supplier<Mat4> supplier) {
		super(ID, shaderName, supplier);
	}

	public static IdSupplierPair supply(Supplier<Mat4> supplier) {
		return new IdSupplierPair(ID, supplier);
	}
}
