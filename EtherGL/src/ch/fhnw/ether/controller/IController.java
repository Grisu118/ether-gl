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
 */package ch.fhnw.ether.controller;

import java.util.Collection;
import java.util.List;

import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.event.IScheduler;
import ch.fhnw.ether.controller.tool.ITool;
import ch.fhnw.ether.controller.tool.NavigationTool;
import ch.fhnw.ether.render.IRenderer;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.ui.UI;
import ch.fhnw.ether.view.IView;

/**
 * A controller that coordinates both model and associated views. It also handles the relevant events coming from
 * individual views.
 *
 * @author radar
 */
public interface IController {
	/**
	 * Get the controller's model.
	 *
	 * @return the controller's model
	 */
	IScene getScene();

	/**
	 * Set the controller's model. This effectively unhooks the current model from the controller and replaces it with
	 * the new one. If a controller implementation does not implement such behavior it will throw an
	 * {@link java.lang.UnsupportedOperationException}.
	 *
	 * @param model
	 *            to be set
	 */
	void setScene(IScene model);

	/**
	 * Get a list of all views.
	 *
	 * @return list of views
	 */
	List<IView> getViews();

	/**
	 * Get current view (i.e. the view that currently receives events).
	 *
	 * @return the current view
	 */
	IView getCurrentView();

	/**
	 * Enable a list of views for rendering.
	 *
	 * @param views
	 *            list of views to be enabled for rendering or NULL to enable all views
	 */
	void enableViews(Collection<IView> views);

	/**
	 * Request specific view to repaint.
	 */
	void repaintView(IView view);

	/**
	 * Request all views to repaint.
	 */
	void repaintViews();

	/**
	 * Get current tool.
	 *
	 * @return the current tool
	 */
	ITool getCurrentTool();

	/**
	 * Set current tool.
	 *
	 * @param tool
	 *            the tool to be set as current tool
	 */
	void setCurrentTool(ITool tool);

	/**
	 * Get navigation tool.
	 *
	 * @return the navigation tool
	 */
	NavigationTool getNavigationTool();

	/**
	 * Get scheduler.
	 *
	 * @return the scheduler
	 */
	IScheduler getScheduler();

	/**
	 * Get renderer.
	 *
	 * @return the renderer
	 */
	IRenderer getRenderer();

	/**
	 * Get UI.
	 *
	 * @return the ui
	 */
	UI getUI();

	// view listener
	
	void viewCreated(IView view);
	
	void viewDisposed(IView view);
	
	void viewGainedFocus(IView view);

	void viewLostFocus(IView view);
	
	void viewChanged(IView view);

	// key listener

	void keyPressed(IKeyEvent e);

	void keyReleased(IKeyEvent e);

	// pointer listener

	void pointerEntered(IPointerEvent e);

	void pointerExited(IPointerEvent e);

	void pointerPressed(IPointerEvent e);

	void pointerReleased(IPointerEvent e);

	void pointerClicked(IPointerEvent e);

	// pointer motion listener

	void pointerMoved(IPointerEvent e);

	void pointerDragged(IPointerEvent e);

	// pointer scroll listener

	void pointerScrolled(IPointerEvent e);

}
