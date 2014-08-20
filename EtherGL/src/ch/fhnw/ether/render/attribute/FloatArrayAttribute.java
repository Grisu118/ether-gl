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

package ch.fhnw.ether.render.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.media.opengl.GL3;

import ch.fhnw.ether.gl.FloatArrayBuffer;
import ch.fhnw.ether.gl.Program;

public class FloatArrayAttribute extends AbstractAttribute implements IArrayAttribute {
	private final NumComponents numComponents;
	private final List<Supplier<float[]>> suppliers = new ArrayList<>();
	private int stride;
	private int offset;

	public FloatArrayAttribute(String id, String shaderName, NumComponents numComponents) {
		super(id, shaderName);
		this.numComponents = numComponents;
	}

	public List<Supplier<float[]>> getSuppliers() {
		return suppliers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addSupplier(Supplier<?> supplier) {
		suppliers.add((Supplier<float[]>)supplier);
	}

	@Override
	public NumComponents getNumComponents() {
		return numComponents;
	}

	@Override
	public void enable(GL3 gl, Program program, FloatArrayBuffer buffer) {
		buffer.enableAttribute(gl, getShaderIndex(gl, program), numComponents, stride, offset);
	}

	@Override
	public void disable(GL3 gl, Program program, FloatArrayBuffer buffer) {
		buffer.disableAttribute(gl, getShaderIndex(gl, program));
	}

	@Override
	public void setup(int stride, int offset) {
		this.stride = stride;
		this.offset = offset;
	}

	@Override
	protected int resolveShaderIndex(GL3 gl, Program program, String shaderName) {
		return program.getAttributeLocation(gl, shaderName);
	}
	
	@Override
	public String toString() {
		String s = super.toString() + "[components=" + numComponents + " stride=" + stride + " offset=" + offset + " suppliers=[";
		for (int i = 0; i < suppliers.size(); ++i) {
			s += suppliers.get(i);
			if (i < suppliers.size() - 1)
				s += ", ";
		}
		s += "]]";
		return s;
	}
}
