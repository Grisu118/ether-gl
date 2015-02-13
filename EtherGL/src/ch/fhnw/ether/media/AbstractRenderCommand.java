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

package ch.fhnw.ether.media;

import ch.fhnw.util.ClassUtilities;
import ch.fhnw.util.IObjectID;
import ch.fhnw.util.Log;

public abstract class AbstractRenderCommand<T extends IRenderTarget, S extends PerTargetState<T>> implements IObjectID {
	private static final Log log = Log.create();

	private final long id = ClassUtilities.createObjectID();

	protected final Parameter[] parameters;
	
	protected AbstractRenderCommand(Parameter ... parameters) {
		this.parameters = new Parameter[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			parameters[i].setIdx(i);
			this.parameters[i] = parameters[i].copy();
		}
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}

	public String getName(Parameter p) {
		return parameters[p.getIdx()].getName();
	}

	public String getDescription(Parameter p) {
		return parameters[p.getIdx()].getDescription();
	}

	public float getMin(Parameter p) {
		return parameters[p.getIdx()].getMin();
	}

	public float getMax(Parameter p) {
		return parameters[p.getIdx()].getMax();
	}

	public float getVal(Parameter p) {
		return parameters[p.getIdx()].getVal();
	}

	public void setVal(Parameter p, float val) {
		parameters[p.getIdx()].setVal(val);
	}

	protected abstract void run(S state) throws RenderCommandException;
	
	@SuppressWarnings({"unused","unchecked" })
	protected S createState(T target) throws RenderCommandException {
		return (S)new Stateless<>(target);
	}

	@SuppressWarnings({"unchecked" })
	protected S getState(T target) {
		try {
			return (S)target.getState(this);
		} catch (RenderCommandException e) {
			log.severe(e);
			return null;
		}
	}

	@Override
	public final long getObjectID() {
		return id;
	}
	
	@Override
	public final int hashCode() {
		return (int) id;
	}
	
	@Override
	public final boolean equals(Object obj) {
		return obj instanceof AbstractRenderCommand && ((AbstractRenderCommand<?,?>)obj).id == id;
	}
	
	@SuppressWarnings("unchecked")
	final void runInternal(IRenderTarget target) throws RenderCommandException {
		run((S)target.getState(this));
	}
	
	@SuppressWarnings("unchecked")
	final <TS extends PerTargetState<?>> TS createStateInternal(IRenderTarget target) throws RenderCommandException {
		return (TS)createState((T) target);
	}
	
	@Override
	public String toString() {
		return getClass().getName();
	}
}
