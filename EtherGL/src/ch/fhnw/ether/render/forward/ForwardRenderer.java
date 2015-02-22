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

package ch.fhnw.ether.render.forward;

import org.lwjgl.opengl.GL11;

import ch.fhnw.ether.render.AbstractRenderer;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.view.IView;

/*
 * General flow:
 * - foreach viewport
 * -- only use geometry assigned to this viewport
 * 
 * - foreach pass
 * -- setup opengl params specific to pass
 * 
 * - foreach material
 * -- enable shader
 * -- write uniforms
 * 
 * - foreach material instance (texture set + uniforms)
 * -- setup texture
 * -- write uniforms
 * -- refresh buffers
 * 
 * - foreach buffer (assembled objects)
 * -- setup buffer
 * -- draw
 */

/**
 * Simple and straightforward forward renderer.
 *
 * @author radar
 */
public class ForwardRenderer extends AbstractRenderer {

	public ForwardRenderer() {
	}

	// FIXME: we should not pass view here, as it might be modified while we're rendering...
	@Override
	public void render(IView view) {
		boolean interactive = view.getConfig().getViewType() == IView.ViewType.INTERACTIVE_VIEW;

		update(view.getCameraMatrices(), view.getViewport());

		getCameras().setCameraSpace();

		// ---- 1. DEPTH QUEUE (DEPTH WRITE&TEST ENABLED, BLEND OFF)
		// FIXME: where do we deal with two-sided vs one-sided? mesh options? shader dependent?
		//GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1, 3);
		renderObjects(Queue.DEPTH, interactive);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		//gl.glDisable(GL.GL_CULL_FACE);

		if (false)
			renderShadowVolumes(Queue.DEPTH, interactive);

		// ---- 2. TRANSPARENCY QUEUE (DEPTH WRITE DISABLED, DEPTH TEST ENABLED, BLEND ON)
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		renderObjects(Queue.TRANSPARENCY, interactive);

		// ---- 3. OVERLAY QUEUE (DEPTH WRITE&TEST DISABLED, BLEND ON)
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		renderObjects(Queue.OVERLAY, interactive);

		// ---- 4. DEVICE SPACE OVERLAY QUEUE (DEPTH WRITE&TEST DISABLED, BLEND ON)
		getCameras().setOrthoDeviceSpace();
		renderObjects(Queue.DEVICE_SPACE_OVERLAY, interactive);

		// ---- 5. SCREEN SPACE OVERLAY  QUEUE(DEPTH WRITE&TEST DISABLED, BLEND ON)
		getCameras().setOrthoScreenSpace();
		renderObjects(Queue.SCREEN_SPACE_OVERLAY, interactive);

		// ---- 6. CLEANUP: RETURN TO DEFAULTS
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}
}
