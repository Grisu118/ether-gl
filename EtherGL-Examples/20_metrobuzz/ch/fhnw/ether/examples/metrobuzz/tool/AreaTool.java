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

package ch.fhnw.ether.examples.metrobuzz.tool;

import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.MeshLibrary;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.ProjectionUtil;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.Line;
import ch.fhnw.util.math.geometry.Plane;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

public final class AreaTool extends AbstractTool {
	private static final RGBA TOOL_COLOR = RGBA.YELLOW;

	private static final float KEY_INCREMENT = 0.01f;

	private final DefaultMesh mesh;

	private boolean moving = false;

	private float xOffset = 0;
	private float yOffset = 0;

	public AreaTool(IController controller) {
		super(controller);
		IGeometry geometry = DefaultGeometry.createV(Primitive.TRIANGLES, MeshLibrary.UNIT_CUBE_TRIANGLES);
		mesh = new DefaultMesh(new ColorMaterial(TOOL_COLOR), geometry);
		mesh.setTransform(Mat4.scale(0.1f, 0.1f, 0.001f));
	}

	@Override
	public void activate() {
		getController().getRenderer().addMesh(mesh);
	}

	@Override
	public void deactivate() {
		getController().getRenderer().removeMesh(mesh);
	}

	@Override
	public void keyPressed(KeyEvent e, IView view) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			yOffset += KEY_INCREMENT;
			break;
		case KeyEvent.VK_DOWN:
			yOffset -= KEY_INCREMENT;
			break;
		case KeyEvent.VK_LEFT:
			xOffset -= KEY_INCREMENT;
			break;
		case KeyEvent.VK_RIGHT:
			xOffset += KEY_INCREMENT;
			break;
		}

		mesh.setPosition(new Vec3(xOffset, yOffset, 0));
		view.getController().repaintViews();
	}

	@Override
	public void mousePressed(MouseEvent e, IView view) {
		int x = e.getX();
		int y = view.getViewport().h - e.getY();
		float d = PickUtilities.pickBoundingBox(PickMode.POINT, x, y, 0, 0, view, mesh.getBounds());
		if (d < Float.POSITIVE_INFINITY)
			moving = true;
	}

	@Override
	public void mouseDragged(MouseEvent e, IView view) {
		if (moving) {
			Line line = ProjectionUtil.getRay(view, e.getX(), view.getViewport().h - e.getY());
			Plane plane = new Plane(new Vec3(0, 0, 1));
			Vec3 p = plane.intersection(line);
			if (p != null) {
				xOffset = p.x;
				yOffset = p.y;
				mesh.setPosition(new Vec3(xOffset, yOffset, 0));
				view.getController().repaintViews();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e, IView view) {
		moving = false;
	}
}
