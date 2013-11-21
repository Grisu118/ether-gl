/*
Copyright (c) 2013, ETH Zurich (Stefan Mueller Arisona, Eva Friedrich)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
  this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
 * Neither the name of ETH Zurich nor the names of its contributors may be 
  used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ch.ethz.ether.scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL2;

import ch.ethz.ether.gl.DrawingUtilities;
import ch.ethz.ether.view.IView;
import ch.ethz.ether.view.IView.ViewType;

public abstract class AbstractTool implements ITool {
	public static final int SNAP_SIZE = 4;
	
	private boolean enabled = true;
	private boolean exclusive = false;

	protected void renderUI(GL2 gl, IView view, String[] text) {
		if (view.getViewType() == ViewType.INTERACTIVE_VIEW) {
			DrawingUtilities.setTextColor(view, 1.0f, 1.0f, 1.0f, 0.5f);
			for (int i = 0; i < text.length; ++i) {
				DrawingUtilities.drawTextRaster(view, 1, 30 + i + 1, text[i]);
			}
		}
	}
	
	protected void renderGrid(GL2 gl, IView view) {
		view.getScene().getNavigationGrid().render(gl, view);
	}
	
	@Override
	public boolean isExclusive() {
		return exclusive;
	}
	
	@Override
	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}
	
	@Override
	public final boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	// draw routine
	@Override
	public void render3D(GL2 gl, IView view) {
	}
	
	@Override
	public void render2D(GL2 gl, IView view) {
	}
	

	// key listener

	@Override
	public void keyPressed(KeyEvent e, IView view) {
	}

	// mouse listener

	@Override
	public void mousePressed(MouseEvent e, IView view) {
	}

	@Override
	public void mouseReleased(MouseEvent e, IView view) {
	}

	// mouse motion listener

	@Override
	public void mouseMoved(MouseEvent e, IView view) {
	}

	@Override
	public void mouseDragged(MouseEvent e, IView view) {
	}

	// mouse wheel listener

	@Override
	public void mouseWheelMoved(MouseWheelEvent e, IView view) {
	}
	
	
	public static final boolean snap2D(int mx, int my, int x, int y) {
		if ((mx >= x - SNAP_SIZE) && (mx <= x + SNAP_SIZE) && (my >= y - SNAP_SIZE) && (my < y + SNAP_SIZE))
			return true;
		return false;
	}	
}
