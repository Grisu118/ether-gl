/*
 * Copyright (c) 2013 - 2014, ETH Zurich & FHNW (Stefan Muller Arisona)
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
 *  Neither the name of ETH Zurich nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
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

package ch.ethz.ether.scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;
import java.util.List;

import ch.ethz.ether.model.IModel;
import ch.ethz.ether.render.IRenderer;
import ch.ethz.ether.tools.ITool;
import ch.ethz.ether.tools.NavigationTool;
import ch.ethz.ether.ui.UI;
import ch.ethz.ether.view.IView;

/**
 * A 'scene' is the controller that coordinates both model and associated views.
 * It also handles the relevant events coming from individual views.
 *
 * @author radar
 */
public interface IScene {
    /**
     * Get the scene's model.
     *
     * @return model the scene's model
     */
    IModel getModel();

    /**
     * Set the scene's model. This effectively unhooks the current model from
     * the scene and replaces it with the new one. If a scene implementation
     * does not implement such behavior it will throw an
     * {@link java.lang.UnsupportedOperationException}.
     *
     * @param model to be set
     */
    void setModel(IModel model);

    /**
     * Add a view to the scene.
     *
     * @param view the view to add
     */
    void addView(IView view);

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
     * @param views list of views to be enabled for rendering or NULL to enable
     *              all views
     */
    void enableViews(Collection<IView> views);

    /**
     * Repaint all views.
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
     * @param tool the tool to be set as current tool
     */
    void setCurrentTool(ITool tool);

    /**
     * Get navigation tool.
     *
     * @return the navigation tool
     */
    NavigationTool getNavigationTool();

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

    // key listener

    void keyPressed(KeyEvent e, IView view);

    void keyReleased(KeyEvent e, IView view);

    void keyTyped(KeyEvent e, IView view);

    // mouse listener

    void mouseEntered(MouseEvent e, IView view);

    void mouseExited(MouseEvent e, IView view);

    void mousePressed(MouseEvent e, IView view);

    void mouseReleased(MouseEvent e, IView view);

    void mouseClicked(MouseEvent e, IView view);

    // mouse motion listener

    void mouseMoved(MouseEvent e, IView view);

    void mouseDragged(MouseEvent e, IView view);

    // mouse wheel listener

    void mouseWheelMoved(MouseWheelEvent e, IView view);
}
